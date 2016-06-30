import cloudsimulator.clustersimulator.ClusterManager;
import cloudsimulator.clustersimulator.dto.Cluster;
import org.junit.Test;

public class TestClusterManager {

    @Test
    public void createClusterManager() {
        ClusterManager clusterManager = new ClusterManager();
        Cluster cluster = clusterManager.getCluster();
    }

    @Test
    public void readMaxRps() {
        ClusterManager clusterManager = new ClusterManager();
        long maxRps = clusterManager.computeMaxRps();
        System.out.println("Max rps: " + maxRps);
    }

}
