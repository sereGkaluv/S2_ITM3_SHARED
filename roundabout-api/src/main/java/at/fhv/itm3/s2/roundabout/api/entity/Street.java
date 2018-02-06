package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.AbstractProSumer;
import at.fhv.itm3.s2.roundabout.api.util.observable.ObserverType;
import at.fhv.itm3.s2.roundabout.api.util.observable.RoundaboutObservable;
import desmoj.core.simulator.Model;
import javafx.beans.value.ObservableValueBase;
import desmoj.core.simulator.TimeSpan;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

public abstract class Street extends AbstractProSumer implements ICarCountable {

    private final String id;
    private long enteredCarsCounter;
    private long leftCarsCounter;
    private long lostCarsCounter;
    private TrafficLight trafficLight;

    protected Observable carObserver;
    protected Observable enteredCarObserver;
    protected Observable leftCarObserver;
    protected Observable carPositionObserver;
    protected Observable trafficLightObserver;

    public Street(Model owner, String name, boolean showInTrace) {
        this(owner, name, showInTrace, false);
    }

    public Street(String id, Model owner, String name, boolean showInTrace) {
        this(id, owner, name, showInTrace, false, 0, 0);
    }

    public Street(Model owner, String name, boolean showInTrace, boolean trafficLightActive) {
        this(UUID.randomUUID().toString(), owner, name, showInTrace, trafficLightActive, 0, 0);
    }

    public Street(String id, Model owner, String name, boolean showInTrace,
                  boolean trafficLightActive, long greenPhaseDuration, long redPhaseDuration) {
        super(owner, name, showInTrace);

        this.id = id;
        this.enteredCarsCounter = 0;
        this.leftCarsCounter = 0;
        this.lostCarsCounter = 0;

        this.trafficLight = new TrafficLight(trafficLightActive, greenPhaseDuration, redPhaseDuration);

        this.carObserver = new RoundaboutObservable();
        this.enteredCarObserver = new RoundaboutObservable();
        this.leftCarObserver = new RoundaboutObservable();
        this.carPositionObserver = new RoundaboutObservable();
        this.trafficLightObserver = new RoundaboutObservable();
    }

    public String getId() {
        return id;
    }

    /**
     * Triggers active traffic light to red if there is a jam in the next street section {@Link Street}
     *
     * @return void
     */
    public void trafficLightActiveAndJamInNextSection() { return; }

    /**
     * Gets total car counter passed into {@code this} {@link Street}.
     *
     * @return total car counter.
     */
    @Override
    public long getNrOfEnteredCars() {
        return enteredCarsCounter;
    }

    /**
     * Gets total car counter passed from {@code this} {@link Street}.
     *
     * @return total car counter.
     */
    @Override
    public long getNrOfLeftCars() { return leftCarsCounter; }

    /**
     * Gets total car counter lost in {@code this} {@link Street}.
     *
     * @return total car counter.
     */
    @Override
    public long getNrOfLostCars() {
        return lostCarsCounter;
    }

    /**
     * Internal method for counter incrementation.
     */
    protected void incrementEnteredCarCounter() {
        this.enteredCarsCounter++;
        this.enteredCarObserver.notifyObservers(this.enteredCarsCounter);
    }

    /**
     * Internal method for counter incrementation.
     */
    protected void incrementLeftCarCounter() {
        this.leftCarsCounter++;
        this.leftCarObserver.notifyObservers(this.leftCarsCounter);
    }

    /**
     * Internal method for counter incrementation.
     */
    protected void incrementLostCarCounter() {
        this.lostCarsCounter++;
    }

    /**
     * Gets physical length of the street section.
     *
     * @return The length in meters.
     */
    public abstract double getLength();

    /**
     * Adds a new car to the street section.
     *
     * @param car The car to add.
     */
    public abstract void addCar(ICar car);

    /**
     * Gets first car in Section.
     *
     * @return first car in section.
     */
    public abstract ICar getFirstCar();

    /**
     * Gets last car in Section.
     *
     * @return last car in section.
     */
    public abstract ICar getLastCar();

    /**
     * Returns car queue of this {@link Street}.
     *
     * @return unmodifiable car queue.
     * @throws IllegalStateException in case if queue equals null.
     */
    public abstract List<ICar> getCarQueue()
    throws IllegalStateException;

    /**
     * Removes the first car of the queue and returns the first car.
     *
     * @return removed car.
     */
    public abstract ICar removeFirstCar();

    /**
     * Checks if the street section is empty.
     *
     * @return True if street section is empty.
     */
    public abstract boolean isEmpty();

    /**
     * Gets the next street connector if available.
     *
     * @return reference to next {@link IStreetConnector}.
     */
    public abstract IStreetConnector getNextStreetConnector();

    /**
     * Gets the previous street connector if available.
     *
     * @return reference to previous {@link IStreetConnector}.
     */
    public abstract IStreetConnector getPreviousStreetConnector();

    /**
     * Sets the previous street connector
     *
     * @param previousStreetConnector street connector to be set
     */
    public abstract void setPreviousStreetConnector(IStreetConnector previousStreetConnector);

    /**
     *  Sets the next street connector
     *
     * @param nextStreetConnector street connector to be set
     */
    public abstract void setNextStreetConnector(IStreetConnector nextStreetConnector);

    /**
     * Gets all car positions of the street section.
     *
     * @return unmodifiable map of car positions.
     */
    public abstract Map<ICar, Double> getCarPositions();

    /**
     * Recalculates all car positions in the street section,
     * starting from the very first car to very last car in section.
     */
    public abstract void updateAllCarsPositions();

    /**
     * Checks if the first car in the street section is on the exit point.
     *
     * @return true if car is on exit point, otherwise false.
     */
    public abstract boolean isFirstCarOnExitPoint();

    /**
     * Checks if first car in street section is able to enter the next section, depending on its predefined route.
     *
     * @return true = car can enter next section, false = car can not enter next section
     */
    public abstract boolean firstCarCouldEnterNextSection();

    /**
     * Checks if there is enough space in the section, depending on the car's length.
     *
     * @param length length of the car
     * @return true = enough space, false = not enough space
     */
    public abstract boolean isEnoughSpace(double length);

    /**
     * Moves the first car from the current section to the next section.
     * In background removes the first car (if there is one) from the queue and puts it into the
     * queue of the next {@link Street} present in car route.
     *
     * @throws IllegalStateException if car cannot move further e.g. next section is null.
     */
    public abstract void moveFirstCarToNextSection()
    throws IllegalStateException;

    /**
     * Calculates either a car could enter next section or not
     *
     * @return true if car could enter next section otherwise false
     */
    public abstract boolean carCouldEnterNextSection();

    /**
     * Returns if traffic light at end of the street is active or not.
     *
     * @return true = active
     */
    public boolean isTrafficLightActive() {
        return trafficLight.isActive();
    }

    /**
     * Returns if traffic light at end of the street is triggered by traffic jam. if not it is cyclic.
     *
     * @return true = active
     */
    public boolean isTrafficLightTriggeredByJam() {
        return trafficLight.isTriggersByJam();
    }

    /**
     * Indicates whether the traffic light at the end of the street signals "free to go" (true) or "stop" (false), if it is active.
     * Otherwise it will always return true.
     *
     *  @return true = free to go
     */
    public boolean isTrafficLightFreeToGo() {
        return !trafficLight.isActive() || trafficLight.isFreeToGo();
    }

    /**
     * Sets the traffic light state (free to go = true, stop = false).
     *
     * @param isFreeToGo set true if cars are free to go
     * @throws IllegalStateException if traffic light is inactive
     */
    public void setTrafficLightFreeToGo(boolean isFreeToGo) throws IllegalStateException {
        trafficLight.setFreeToGo(isFreeToGo);
        trafficLightObserver.notifyObservers(isFreeToGo);
    }

    /**
     * Getter for red phase duration of traffic light
     *
     * @return the duration of the red light
     */
    public long getRedPhaseDurationOfTrafficLight() { return this.trafficLight.getRedCircleDuration(); }

    /**
     * Getter for green phase duration of traffic light
     *
     * @return the duration of the green light
     */
    public long getGreenPhaseDurationOfTrafficLight() { return this.trafficLight.getGreenCircleDuration(); }

    /**
     * Helper method that registers typed observers.
     *
     * @param observerType type of observer to be registered.
     * @param o observer.
     */
    public synchronized void addObserver(ObserverType observerType, Observer o) {
        switch (observerType) {
            case CAR_ENTERED: enteredCarObserver.addObserver(o); break;
            case CAR_ENTITY: carObserver.addObserver(o); break;
            case CAR_LEFT: leftCarObserver.addObserver(o); break;
            case CAR_POSITION: carPositionObserver.addObserver(o); break;
            case TRAFFIC_LIGHT: trafficLightObserver.addObserver(o); break;
        }
    }
}
