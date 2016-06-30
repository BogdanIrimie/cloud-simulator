package cloudsimulator.requestsimulator.simulators;

import cloudsimulator.requestsimulator.dto.SimulationStatistics;
import cloudsimulator.requestsimulator.logparser.LogParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Simulator {

    @Autowired
    LogParser logParser;

    public void startSimulation() throws IOException {
        SimulationStatistics simulationStatistics = logParser.parseLogs();
        double avgResponseTime = simulationStatistics.getTotalDelay()
                / simulationStatistics.getFulfilledRequestCounter();
        System.out.printf("Average response time is: %f\n", avgResponseTime);
        System.out.println("Number of requests dropped: " + simulationStatistics.getTimeOutedRequestCounter());
    }

}
