package cloudsimulator.clustersimulator.dto;

import java.util.List;

/**
 * ClusterManager contains a list a treatment category groups.
 */
public class Cluster {
    private List<TCGroup> cluster;

    public Cluster() {
    }

    public Cluster(List<TCGroup> cluster) {
        this.cluster = cluster;
    }

    public List<TCGroup> getCluster() {
        return cluster;
    }

    public void setCluster(List<TCGroup> cluster) {
        this.cluster = cluster;
    }
}
