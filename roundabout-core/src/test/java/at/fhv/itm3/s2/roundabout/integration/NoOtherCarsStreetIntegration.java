package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.mocks.RouteGenerator;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class NoOtherCarsStreetIntegration {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
    }

    @Test
    public void noOtherCarsStreet_noStop() {

        exp.stop(new TimeInstant(60,TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);

        IRoute route = routeGenerator.getRoute(RouteType.TWO_STREETSECTIONS_ONE_CAR);
        AbstractSource source = route.getSource();

        source.startGeneratingCars();

        Street sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals("car doesn't stop", 1, sink.getNrOfEnteredCars());
    }

}
