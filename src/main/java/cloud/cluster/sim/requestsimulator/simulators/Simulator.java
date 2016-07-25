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
    TraceParser traceParser;

    @Autowired
    SimulationStatisticsOperations simulationStatisticsOperations;

    private static final Logger logger = LoggerFactory.getLogger(Simulator.class);

    public void startSimulation() throws IOException {
        SimulationStatistics simulationStatistics = traceParser.parseTraces();
        double avgResponseTime = simulationStatistics.getTotalDelay()
                / simulationStatistics.getFulfilledRequestCounter();

        simulationStatisticsOperations.insert(simulationStatistics);
        logger.info("Average response time was:      " + String.format("%.10f", avgResponseTime));
        logger.info("Number of requests dropped:     " + simulationStatistics.getTimeOutedRequestCounter());
        logger.info("Total cost:                     " + simulationStatistics.getTotalCost());
    }

}
