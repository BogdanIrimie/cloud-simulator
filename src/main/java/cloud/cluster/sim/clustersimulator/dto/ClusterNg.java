package cloud.cluster.sim.clustersimulator.dto;

import org.springframework.stereotype.Component;

import java.util.List;

public class ClusterNg {

    private List<Vm> vms;

    public ClusterNg(List<Vm> vms) {
        this.vms = vms;
    }

    public List<Vm> getVms() {
        return vms;
    }

    public void setVms(List<Vm> vms) {
        this.vms = vms;
    }
}
