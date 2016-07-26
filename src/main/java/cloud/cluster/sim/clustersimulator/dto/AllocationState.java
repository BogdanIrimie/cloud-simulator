package cloud.cluster.sim.clustersimulator.dto;

public class AllocationState {
    private double time;
    private int vmNumber;
    private Reason reason;

    public AllocationState(double time, int vmNumber, Reason reason) {
        this.time = time;
        this.vmNumber = vmNumber;
        this.reason = reason;
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

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }
}
