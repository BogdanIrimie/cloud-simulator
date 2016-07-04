package cloudsimulator.requestsimulator.dto;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

/**
 * Output of a simulation.
 */
@Component
public class SimulationStatistics {

    @Id
    private String id;
    private double totalDelay;
    private long totalRequestCounter;
    private long fulfilledRequestCounter;
    private long timeOutedRequestCounter;
    private long totalCost;

    public SimulationStatistics() {
    }

    public SimulationStatistics(double totalDelay, long totalRequestCounter, long fulfilledRequestCounter, long timeOutedRequestCounter, long totalCost) {
        this.totalDelay = totalDelay;
        this.totalRequestCounter = totalRequestCounter;
        this.fulfilledRequestCounter = fulfilledRequestCounter;
        this.timeOutedRequestCounter = timeOutedRequestCounter;
        this.totalCost = totalCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalDelay() {
        return totalDelay;
    }

    public void setTotalDelay(double totalDelay) {
        this.totalDelay = totalDelay;
    }

    public long getTotalRequestCounter() {
        return totalRequestCounter;
    }

    public void setTotalRequestCounter(long totalRequestCounter) {
        this.totalRequestCounter = totalRequestCounter;
    }

    public long getFulfilledRequestCounter() {
        return fulfilledRequestCounter;
    }

    public void setFulfilledRequestCounter(long fulfilledRequestCounter) {
        this.fulfilledRequestCounter = fulfilledRequestCounter;
    }

    public long getTimeOutedRequestCounter() {
        return timeOutedRequestCounter;
    }

    public void setTimeOutedRequestCounter(long timeOutedRequestCounter) {
        this.timeOutedRequestCounter = timeOutedRequestCounter;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }
}
