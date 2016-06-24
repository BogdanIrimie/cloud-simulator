package cloudsimulator.clustersimulator;


import cloudsimulator.clustersimulator.dto.SimulatedCluster;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Read cluster configuration from file.
 */
public class ConfigurationReader {

    public SimulatedCluster readClusterConfiguration() {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("clusterConfiguration.json").getFile();

        ObjectMapper mapper = new ObjectMapper();
        SimulatedCluster simulatedCluster = new SimulatedCluster();

        try {
            simulatedCluster = mapper.readValue(new File(path), SimulatedCluster.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return simulatedCluster;
    }
}
