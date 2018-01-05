package at.fhv.itm3.s2.roundabout.api.entity;

import desmoj.core.simulator.Model;

import java.util.Map;
import java.util.Set;

public interface IRoundaboutStructure {

    /**
     * Add a street connector to the structure.
     * Structure setup needs to be done before.
     *
     * @param streetConnector street connector to be added
     */
    void addStreetConnector(IStreetConnector streetConnector);

    /**
     * Add a route to the structure.
     * Structure setup needs to be done before.
     *
     * @param route route to be added
     */
    void addRoute(IRoute route);

    /**
     * Add a street to the structure.
     * Structure setup needs to be done before.
     *
     * @param street street to be added
     */
    void addStreet(Street street);

    /**
     * Add a configuration parameter to the structure.
     * Known parameters: //TODO full list of parameters
     *
     * @param key   key of the parameter
     * @param value value of the parameter
     */
    void addParameter(String key, String value);

    /**
     * Get all street connectors of the structure.
     *
     * @return street connectors of structure
     */
    Set<IStreetConnector> getStreetConnectors();

    /**
     * Get all routes of the structure.
     *
     * @return routes of structure
     */
    Set<IRoute> getRoutes();

    /**
     * Get all streets of the structure.
     *
     * @return streets of structure
     */
    Set<Street> getStreets();

    /**
     * Get all parameters of the structure.
     *
     * @return parameters of structure
     */
    Map<String, String> getParameters();

    /**
     * Get simulation model of structure.
     *
     * @return simulation model of structure
     */
    Model getModel();
}