package cloudsimulator.clustersimulator.dto;

import java.util.List;

/**
 * Cluster contains a list a treatment category groups.
 */
public class SimulatedCluster {
    private List<TCGroup> cluster;

    public SimulatedCluster() {
    }

    public SimulatedCluster(List<TCGroup> cluster) {
        this.cluster = cluster;
    }

    public List<TCGroup> getCluster() {
        return cluster;
    }

    public void setCluster(List<TCGroup> cluster) {
        this.cluster = cluster;
    }
}
