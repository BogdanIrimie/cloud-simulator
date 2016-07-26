package cloud.cluster.sim.clustersimulator.dto;

public class AllocationState {
    private double time;
    private int vmNumber;

    public AllocationState(double time, int vmNumber) {
        this.time = time;
        this.vmNumber = vmNumber;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getVmNumber() {
        return vmNumber;
    }

    public void setVmNumber(int vmNumber) {
        this.vmNumber = vmNumber;
    }
}
