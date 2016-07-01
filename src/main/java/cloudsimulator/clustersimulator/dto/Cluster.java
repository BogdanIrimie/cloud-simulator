package cloudsimulator.clustersimulator.dto;

import java.util.List;

/**
 * ClusterManager contains a list a treatment category groups.
 */
public class Cluster {
    private List<TCGroup> tgGroup;

    public Cluster() {
    }

    public Cluster(List<TCGroup> tgGroup) {
        this.tgGroup = tgGroup;
    }

    public List<TCGroup> getTgGroup() {
        return tgGroup;
    }

    public void setTgGroup(List<TCGroup> tgGroup) {
        this.tgGroup = tgGroup;
    }
}
