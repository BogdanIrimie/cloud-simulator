import cloud.cluster.sim.clustersimulator.ClusterManager;
import org.junit.Test;

public class TestClusterManager {

    @Test
    public void createClusterManager() {
        ClusterManager clusterManager = new ClusterManager();
        int clusterSize = clusterManager.getClusterSize();
    }

    @Test
    public void readMaxOps() {
        ClusterManager clusterManager = new ClusterManager();
        long maxOps = clusterManager.computeCumulativeOpsForCluster();
        System.out.println("Max ops: " + maxOps);
    }

}
