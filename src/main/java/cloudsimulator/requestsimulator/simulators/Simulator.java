package cloudsimulator.requestsimulator.simulators;

import cloudsimulator.requestsimulator.dao.SimulationStatisticsOperations;
import cloudsimulator.requestsimulator.dto.SimulationStatistics;
import cloudsimulator.requestsimulator.logparser.TraceParser;
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
        logger.info("Average response time was:      {}", avgResponseTime);
        logger.info("Number of requests dropped:     " + simulationStatistics.getTimeOutedRequestCounter());
        logger.info("Total cost:                     " + simulationStatistics.getTotalCost());
    }

}
