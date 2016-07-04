package cloudsimulator.requestsimulator.simulators;

import cloudsimulator.requestsimulator.dto.SimulationStatistics;
import cloudsimulator.requestsimulator.logparser.LogParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Simulator {

    @Autowired
    LogParser logParser;

    private static final Logger logger = LoggerFactory.getLogger(Simulator.class);

    public void startSimulation() throws IOException {
        SimulationStatistics simulationStatistics = logParser.parseLogs();
        double avgResponseTime = simulationStatistics.getTotalDelay()
                / simulationStatistics.getFulfilledRequestCounter();

        logger.info("Average response time was:      {}", avgResponseTime);
        logger.info("Number of requests dropped:     " + simulationStatistics.getTimeOutedRequestCounter());
    }

}
