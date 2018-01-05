package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.*;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutIntersection;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetConnector;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;

import java.util.*;

public class RouteGenerator {

    private Map<Integer, IRoute> routes;
    private RoundaboutSimulationModel model;

    public RouteGenerator(RoundaboutSimulationModel model) {
        routes = new HashMap<>();
        this.model = model;

        initializeRouteWithTwoStreetSections();
        initializeRouteWithIntersection();
        initializeRouteWithTwoTracksAndTwoStreetSectionsPerTrack();
    }

    private IRoute getRouteWithTwoStreetSections() {
        return routes.get(0);
    }

    private IRoute getRouteWithIntersectionAndTwoStreetSections() { return routes.get(1); }

    private IRoute getRouteWhereOneCarStaysOnTrack() { return routes.get(2); }

    private IRoute getRouteWhereOneCarChangesTrack() { return routes.get(3); }

    public IRoute getRoute(RouteType type) {
        switch (type) {
            case TWO_STREETSECTIONS:
                return getRouteWithTwoStreetSections();
            case STREETSECTION_INTERSECTION_STREETSECTION:
                return getRouteWithIntersectionAndTwoStreetSections();
            case ONE_CAR_STAYS_ON_TRACK:
                return getRouteWhereOneCarStaysOnTrack();
            case ONE_CAR_CHANGES_TRACK:
                return getRouteWhereOneCarChangesTrack();
        }
        return null;
    }

    private void initializeRouteWithTwoStreetSections() {

        // INITIALIZE ROUTE WITH TWO STREETSECTIONS
        // initialize streets and sink
        Street street1_1 = new StreetSection(10.0, model, "", false);
        Street street1_2 = new StreetSection(10.0, model, "", false);
        RoundaboutSink sink1 = new RoundaboutSink(model, "", false);

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1_1 = new LinkedList<>();
        prevStreetsForConnector1_1.add(street1_1);

        List<IConsumer> nextStreetsForConnector1_1 = new LinkedList<>();
        nextStreetsForConnector1_1.add(street1_2);

        StreetConnector connector1_1 = new StreetConnector(prevStreetsForConnector1_1, nextStreetsForConnector1_1);
        street1_1.setNextStreetConnector(connector1_1);
        street1_2.setPreviousStreetConnector(connector1_1);
        connector1_1.initializeTrack(street1_1, ConsumerType.STREET_SECTION, street1_2, ConsumerType.STREET_SECTION);

        List<IConsumer> prevStreetsForConnector1_2 = new LinkedList<>();
        prevStreetsForConnector1_2.add(street1_2);

        List<IConsumer> nextStreetsForConnector1_2 = new LinkedList<>();
        nextStreetsForConnector1_2.add(sink1);

        StreetConnector connector1_2 = new StreetConnector(prevStreetsForConnector1_2, nextStreetsForConnector1_2);
        street1_2.setNextStreetConnector(connector1_2);
        sink1.setPreviousStreetConnector(connector1_2);
        connector1_2.initializeTrack(street1_2, ConsumerType.STREET_SECTION, sink1, ConsumerType.STREET_SECTION);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(model, "", false, street1_1, 2, this, RouteType.TWO_STREETSECTIONS);

        IRoute route1 = new Route();
        route1.addSource(source1);
        route1.addSection(street1_1);
        route1.addSection(street1_2);
        route1.addSection(sink1);

        routes.put(0, route1);
    }

    private void initializeRouteWithIntersection() {
        float turnaroundTime = 60;
        float[] phaseShiftTimes = new float[]{0.0F, 10.0F, 20.0F};
        double intersectionTraverseTime = 5.0;
        double accelerationTime = 2.0;
        double yellowDuration = turnaroundTime / 8;
        double greenDuration = turnaroundTime / 2 - yellowDuration;
        int outDirection = 1;
        int inDirection = 0;

        // initialize intersection
        RoundaboutIntersection intersection = new RoundaboutIntersection(model, "", false, 2);
        intersection.setServiceDelay(accelerationTime);
        FixedCirculationController ic = ModelFactory.getInstance(model).createOneWayController(
                intersection,
                greenDuration,
                yellowDuration,
                phaseShiftTimes[0]
        );
        intersection.attachController(ic);

        // initialize streets
        AbstractProSumer street1 = new StreetSection(10.0, model, "", false);
        AbstractProSumer street2 = new StreetSection(10.0, model, "", false);

        // initialize sink
        RoundaboutSink sink = new RoundaboutSink(model, "", false);

        // initialize source
        AbstractSource source = new RoundaboutSourceMock(model, "", false, (StreetSection)street1, 2, this, RouteType.STREETSECTION_INTERSECTION_STREETSECTION);

        // connect streets with intersection
        IntersectionController.getInstance().setIntersectionInDirectionMapping(intersection, street1, inDirection);
        IntersectionController.getInstance().setIntersectionOutDirectionMapping(intersection, street2, outDirection);
        intersection.attachProducer(inDirection, street1.toProducer());
        intersection.attachConsumer(outDirection, street2.toConsumer());
        intersection.createConnectionQueue(street1.toProducer(), new AbstractConsumer[]{street2.toConsumer()}, new double[]{intersectionTraverseTime}, new double[]{1.0});

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1 = new LinkedList<>();
        prevStreetsForConnector1.add(street2);

        List<IConsumer> nextStreetsForConnector1 = new LinkedList<>();
        nextStreetsForConnector1.add(sink);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        ((StreetSection)street2).setNextStreetConnector(connector1);
        sink.setPreviousStreetConnector(connector1);
        connector1.initializeTrack(street2, ConsumerType.STREET_SECTION, sink, ConsumerType.STREET_SECTION);

        // initialize route
        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(intersection);
        route.addSection(street2);
        route.addSection(sink);

        routes.put(1, route);
    }

    private void initializeRouteWithTwoTracksAndTwoStreetSectionsPerTrack() {

        // initialize streets and sink
        Street street1_1 = new StreetSection(100.0, model, "", false);
        Street street1_2 = new StreetSection(100.0, model, "", false);
        RoundaboutSink sink1 = new RoundaboutSinkMock(model, "", false);

        Street street2_1 = new StreetSection(100.0, model, "", false);
        Street street2_2 = new StreetSection(100.0, model, "", false);
        RoundaboutSink sink2 = new RoundaboutSinkMock(model, "", false);

        // initialize connectors
        List<IConsumer> prevStreetsForConnector1 = new LinkedList<>();
        prevStreetsForConnector1.add(street1_1);
        prevStreetsForConnector1.add(street2_1);

        List<IConsumer> nextStreetsForConnector1 = new LinkedList<>();
        nextStreetsForConnector1.add(street1_2);
        nextStreetsForConnector1.add(street2_2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1_1.setNextStreetConnector(connector1);
        street1_2.setPreviousStreetConnector(connector1);
        street2_1.setNextStreetConnector(connector1);
        street2_2.setPreviousStreetConnector(connector1);
        connector1.initializeTrack(street1_1, ConsumerType.STREET_SECTION, street1_2, ConsumerType.STREET_SECTION);
        connector1.initializeTrack(street2_1, ConsumerType.STREET_SECTION, street2_2, ConsumerType.STREET_SECTION);

        List<IConsumer> prevStreetsForConnector2 = new LinkedList<>();
        prevStreetsForConnector2.add(street1_2);
        prevStreetsForConnector2.add(street2_2);

        List<IConsumer> nextStreetsForConnector2 = new LinkedList<>();
        nextStreetsForConnector2.add(sink1);
        nextStreetsForConnector2.add(sink2);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street1_2.setNextStreetConnector(connector2);
        sink1.setPreviousStreetConnector(connector2);
        street2_2.setNextStreetConnector(connector2);
        sink2.setPreviousStreetConnector(connector2);
        connector2.initializeTrack(street1_2, ConsumerType.STREET_SECTION, sink1, ConsumerType.STREET_SECTION);
        connector2.initializeTrack(street2_2, ConsumerType.STREET_SECTION, sink2, ConsumerType.STREET_SECTION);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(model, "", false, street1_1, 1, this, RouteType.ONE_CAR_STAYS_ON_TRACK);
        AbstractSource source2 = new RoundaboutSourceMock(model, "", false, street2_1, 1, this, RouteType.ONE_CAR_CHANGES_TRACK);

        IRoute route1 = new Route();
        route1.addSource(source1);
        route1.addSection(street1_1);
        route1.addSection(street1_2);
        route1.addSection(sink1);

        IRoute route2 = new Route();
        route2.addSource(source2);
        route2.addSection(street2_1);
        route2.addSection(street1_2);
        route2.addSection(sink1);

        routes.put(2, route1);
        routes.put(3, route2);
    }
}