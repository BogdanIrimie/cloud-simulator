import cloud.cluster.sim.clustersimulator.ClusterManager;
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
        long maxRps = clusterManager.computeCumulativeRpsForCluster();
        System.out.println("Max rps: " + maxRps);
    }

}
