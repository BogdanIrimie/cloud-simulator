package cloud.cluster.sim.clustersimulator.dto;

import org.springframework.stereotype.Component;

import java.util.List;

public class Cluster {

    private List<Vm> vms;

    public Cluster() {
    }

    public Cluster(List<Vm> vms) {
        this.vms = vms;
    }

    public List<Vm> getVms() {
        return vms;
    }

    public void setVms(List<Vm> vms) {
        this.vms = vms;
    }
}
