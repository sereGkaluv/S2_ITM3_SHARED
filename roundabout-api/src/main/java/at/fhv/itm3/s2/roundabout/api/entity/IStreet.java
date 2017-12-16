package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.Car;

import java.util.Map;

public interface IStreet {

    /**
     * Gets physical length of the street section.
     *
     * @return The length in meters.
     */
    double getLength();

    /**
     * Adds a new car to the street section.
     *
     * @param car The car to add.
     */
    void addCar(ICar car);

    /**
     * Adds a new car of the old implementation.
     *
     * @param car   the car to add
     */
    void addCar(Car car);

    /**
     * Gets first car in Section.
     *
     * @return first car in section.
     */
    ICar getFirstCar();

    /**
     * Gets last car in Section.
     *
     * @return last car in section.
     */
    ICar getLastCar();

    /**
     * Removes the first car of the queue and returns the first car.
     *
     * @return removed car.
     */
    ICar removeFirstCar();

    /**
     * Checks if the street section is empty.
     *
     * @return True if street section is empty.
     */
    boolean isEmpty();

    /**
     * Gets the next street connector if available.
     *
     * @return reference to next {@link IStreetConnector}.
     */
    IStreetConnector getNextStreetConnector();

    /**
     * Gets the previous street connector if available.
     *
     * @return reference to previous {@link IStreetConnector}.
     */
    IStreetConnector getPreviousStreetConnector();

    /**
     * Sets the previous street connector
     *
     * @param previousStreetConnector
     */
    void setPreviousStreetConnector(IStreetConnector previousStreetConnector);

    /**
     *  Sets the next street connector
     *
     * @param nextStreetConnector
     */
    void setNextStreetConnector(IStreetConnector nextStreetConnector);

    /**
     * Gets all car positions of the street section.
     *
     * @return unmodifiable map of car positions.
     */
    Map<ICar, Double> getCarPositions();

    /**
     * Recalculates all car positions in the street section,
     * starting from the very first car to very last car in section.
     */
    void updateAllCarsPositions();

    /**
     * Checks if the first car in the street section is on the exit point.
     *
     * @return true if car is on exit point, otherwise false.
     */
    boolean isFirstCarOnExitPoint();

    /**
     * Checks if first car in street section is able to enter the next section, depending on its predefined route.
     *
     * @return true = car can enter next section, false = car can not enter next section
     */
    boolean firstCarCouldEnterNextSection();

    /**
     * Checks if there is enough space in the section, depending on the car's length.
     *
     * @param length length of the car
     * @return true = enough space, false = not enough space
     */
    boolean isEnoughSpace(double length);

    /**
     * Moves the first car from the current section to the next section.
     * In background removes the first car (if there is one) from the queue and puts it into the
     * queue of the next {@link IStreet} present in car route.
     *
     * @throws IllegalStateException if car cannot move further e.g. next section is null.
     */
    void moveFirstCarToNextSection()
    throws IllegalStateException;

    @Deprecated // TODO consider removal i think this logic can be packed into addCar method, othervise consider rename to isCarAbleToEnter()
    boolean carCouldEnterNextSection();

    /**
     * Returns the number of cars that have entered the sink
     *
     * @return  the number of cars as int
     */
    int getNrOfEnteredCars();
}