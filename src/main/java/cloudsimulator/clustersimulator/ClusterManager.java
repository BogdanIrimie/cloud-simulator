package cloudsimulator.clustersimulator;

import cloudsimulator.clustersimulator.dto.Cluster;
import cloudsimulator.clustersimulator.dto.TCGroup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Implements operations on the Cluster.
 */
@Component
public class ClusterManager {

    private static final long RPS_FOR_ONE_VM = 100;
    private Cluster cluster;

    /**
     * Read Cluster configuration date from Json file.
     */
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

    /**
     * Return the Cluster used by the ClusterManager.
     *
     * @return Cluster used by the ClusterManager.
     */
    public Cluster getCluster() {
        return cluster;
    }

    public long getRpsForOneVm() {
        return RPS_FOR_ONE_VM;
    }

    /**
     * Compute the maximum RPS supported by the Cluster managed by the ClusterManager.
     *
     * @return maximum number of request that can be fulfilled by the Cluster in one second.
     */
    public long computeMaxRps() {
        return cluster.getTgGroup()
                .stream()
                .mapToLong(TCGroup::getVmNumber)
                .sum() * RPS_FOR_ONE_VM;
    }

}
