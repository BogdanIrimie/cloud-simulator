package cloud.cluster.sim.clustersimulator.dto;

public class Vm {
    private int requestId;
    private double startTime;
    private double endTime;

    public Vm() {
    }

    public Vm(int requestId, double startTime, double endTime) {
        this.requestId = requestId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

}
