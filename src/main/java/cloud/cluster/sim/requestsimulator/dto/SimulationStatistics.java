package cloud.cluster.sim.requestsimulator.dto;

import cloud.cluster.sim.utilities.SimSettingsExtractor;
import cloud.cluster.sim.utilities.dto.SimulationSettings;
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
    private double avgDelay;
    private long executionTime;
    private long totalRequestCounter;
    private long fulfilledRequestCounter;
    private long timeOutedRequestCounter;
    private long totalCost;
    private SimulationSettings simulationSettings;

    public SimulationStatistics() {
    }

    public SimulationStatistics(double totalDelay, long totalRequestCounter, long fulfilledRequestCounter,
                                long timeOutedRequestCounter, long totalCost, long executionTime) {
        this.totalDelay = totalDelay;
        this.totalRequestCounter = totalRequestCounter;
        this.fulfilledRequestCounter = fulfilledRequestCounter;
        this.timeOutedRequestCounter = timeOutedRequestCounter;
        this.totalCost = totalCost;
        this.avgDelay = totalDelay / fulfilledRequestCounter;
        this.executionTime = executionTime;
        this.simulationSettings = SimSettingsExtractor.getSimulationSettings();
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

    public double getAvgDelay() {
        return avgDelay;
    }

    public void setAvgDelay(double avgDelay) {
        this.avgDelay = avgDelay;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
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
