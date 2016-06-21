package cloudSimulator.dto;

import org.springframework.data.annotation.Id;

public class RequestDetails {

    @Id
    private String id;

    private long requestId;
    private double responseTime;

    public RequestDetails() {
    }

    public RequestDetails(long requestId, double responseTime) {
        this.requestId = requestId;
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

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

}
