package cloud.cluster.sim.clustersimulator.dto;

import java.util.List;

/**
 * ClusterManager contains a list a treatment category groups.
 */
public class ClusterExtRep {
    private List<TCGroup> tgGroup;

    public ClusterExtRep() {
    }

    public ClusterExtRep(List<TCGroup> tgGroup) {
        this.tgGroup = tgGroup;
    }

    public List<TCGroup> getTgGroup() {
        return tgGroup;
    }

    public void setTgGroup(List<TCGroup> tgGroup) {
        this.tgGroup = tgGroup;
    }
}
