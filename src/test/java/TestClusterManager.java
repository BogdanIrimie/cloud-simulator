import cloud.cluster.sim.clustersimulator.ClusterManager;
import cloud.cluster.sim.clustersimulator.dto.Cluster;
import org.junit.Test;

public class TestClusterManager {

    @Test
    public void createClusterManager() {
        ClusterManager clusterManager = new ClusterManager();
        int clusterSize = clusterManager.getClusterSize();
    }

    @Test
    public void readMaxRps() {
        ClusterManager clusterManager = new ClusterManager();
        long maxRps = clusterManager.computeMaxRps();
        System.out.println("Max rps: " + maxRps);
    }

}
