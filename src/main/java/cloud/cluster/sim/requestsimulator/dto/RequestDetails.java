package cloud.cluster.sim.requestsimulator.dto;

import org.springframework.data.annotation.Id;

/**
 * Characteristics for request, some obtaines from trace and others computed by the simulation.
 */
public class RequestDetails {

    @Id
    private String id;

    private long requestId;
    private double requestArrivalTime;
    private double responseTime;

    public RequestDetails() {
    }

    public RequestDetails(long requestId, double requestArrivalTime) {
        this.requestId = requestId;
        this.requestArrivalTime = requestArrivalTime;
    }

    public RequestDetails(long requestId, double requestArrivalTime, double responseTime) {
        this.requestId = requestId;
        this.requestArrivalTime = requestArrivalTime;
        this.responseTime = responseTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public double getRequestArrivalTime() {
        return requestArrivalTime;
    }

    public void setRequestArrivalTime(double requestArrivalTime) {
        this.requestArrivalTime = requestArrivalTime;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

}
