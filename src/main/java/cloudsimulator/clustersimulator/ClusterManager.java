package cloudsimulator.clustersimulator;

import cloudsimulator.clustersimulator.dto.Cluster;
import cloudsimulator.clustersimulator.dto.TCGroup;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ClusterManager {

    private static final long RPS_FOR_VM = 100;
    private Cluster cluster;

    public ClusterManager() {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("clusterConfiguration.json").getFile();

        ObjectMapper mapper = new ObjectMapper();

        try {
            cluster = mapper.readValue(new File(path), Cluster.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cluster getCluster() {
        return cluster;
    }

    public long computeMaxRps() {
        return cluster.getCluster()
                .stream()
                .mapToLong(TCGroup::getVmNumber)
                .sum() * RPS_FOR_VM;
    }

}
