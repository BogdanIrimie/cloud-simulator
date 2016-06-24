package cloudsimulator.requestsimulator.simulators;

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
        logParser.parseLogs();
    }

}
