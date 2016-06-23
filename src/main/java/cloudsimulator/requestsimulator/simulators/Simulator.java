package cloudsimulator.requestsimulator.simulators;

import cloudsimulator.requestsimulator.logparser.LogParser;

import java.io.IOException;

public class Simulator {

    public void startSimulation() throws IOException {
        LogParser logParser = new LogParser();
        logParser.parseLogs();
    }

}
