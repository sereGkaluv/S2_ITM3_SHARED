package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.Car;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

import java.util.Map;

public abstract class Street extends AbstractConsumer {

    protected int carCounter;

    public Street(Model model, String s, boolean b) {
        super(model, s, b);
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
     * @param previousStreetConnector
     */
    public abstract void setPreviousStreetConnector(IStreetConnector previousStreetConnector);

    /**
     *  Sets the next street connector
     *
     * @param nextStreetConnector
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

    @Deprecated // TODO consider removal i think this logic can be packed into addCar method, othervise consider rename to isCarAbleToEnter()
    public abstract boolean carCouldEnterNextSection();

    /**
     * Returns the number of cars that have entered the sink
     *
     * @return  the number of cars as int
     */
    public abstract int getNrOfEnteredCars();
}