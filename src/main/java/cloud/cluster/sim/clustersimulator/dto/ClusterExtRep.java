package cloud.cluster.sim.clustersimulator.dto;

import java.util.List;

/**
 * ClusterManager contains a list a treatment category groups.
 */
public class ClusterExtRep {
    private List<MdcGroup> mdcGroup;

    public ClusterExtRep() {
    }

    public ClusterExtRep(List<MdcGroup> mdcGroup) {
        this.mdcGroup = mdcGroup;
    }

    public List<MdcGroup> getMdcGroup() {
        return mdcGroup;
    }

    public void setMdcGroup(List<MdcGroup> mdcGroup) {
        this.mdcGroup = mdcGroup;
    }
}
