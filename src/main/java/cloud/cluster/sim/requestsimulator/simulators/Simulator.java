package cloud.cluster.sim.requestsimulator.simulators;

import cloud.cluster.sim.requestsimulator.logparser.TraceParser;
import cloud.cluster.sim.requestsimulator.dao.SimulationStatisticsOperations;
import cloud.cluster.sim.requestsimulator.dto.SimulationStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Simulator {

    @Autowired
    private TraceParser traceParser;

    @Autowired
    private SimulationStatisticsOperations simulationStatisticsOperations;

    private static final Logger logger = LoggerFactory.getLogger(Simulator.class);

    public void startSimulation() throws IOException {
        SimulationStatistics simulationStatistics = traceParser.parseTraces();

        simulationStatisticsOperations.insert(simulationStatistics);
        logger.info("Average response time was:      " + String.format("%.10f", simulationStatistics.getAvgResponseTime()));
        logger.info("Number of requests dropped:     " + simulationStatistics.getTimeOutedRequestCounter());
        logger.info("Number of request fulfilled:    " + simulationStatistics.getFulfilledRequestCounter());
        logger.info("Total cost:                     " + simulationStatistics.getTotalCost());
    }

}
