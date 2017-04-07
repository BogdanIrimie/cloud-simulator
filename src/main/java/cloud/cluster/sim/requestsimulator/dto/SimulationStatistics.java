package cloud.cluster.sim.requestsimulator.dto;

import cloud.cluster.sim.clustersimulator.dto.AllocationState;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import cloud.cluster.sim.utilities.dto.SimulationSettings;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Output of a simulation.
 */
@Component
public class SimulationStatistics {

    @Id
    private String id;
    private double totalDelay;
    private double avgResponseTime;
    private long executionTime;
    private long totalRequestCounter;
    private long fulfilledRequestCounter;
    private long timeOutedRequestCounter;
    private long totalCost;
    private long simulationTics;
    private SimulationSettings simulationSettings;
    private List<AllocationState> allocationEvolution;

    public SimulationStatistics() {
    }

    public SimulationStatistics(double totalDelay, long totalRequestCounter, long fulfilledRequestCounter,
                                long timeOutedRequestCounter, long totalCost, long executionTime, long simulationTics) {
        this.totalDelay = totalDelay;
        this.totalRequestCounter = totalRequestCounter;
        this.fulfilledRequestCounter = fulfilledRequestCounter;
        this.timeOutedRequestCounter = timeOutedRequestCounter;
        this.simulationTics = simulationTics;
        this.totalCost = totalCost;
        this.avgResponseTime = totalDelay / fulfilledRequestCounter;
        this.executionTime = executionTime;
        this.simulationSettings = SimSettingsExtractor.getSimulationSettings();
    }

    public SimulationStatistics(double totalDelay, long totalRequestCounter, long fulfilledRequestCounter,
                                long timeOutedRequestCounter, long simulationTics, long totalCost,
                                long executionTime, List<AllocationState> allocationEvolution) {
        this.totalDelay = totalDelay;
        this.totalRequestCounter = totalRequestCounter;
        this.fulfilledRequestCounter = fulfilledRequestCounter;
        this.timeOutedRequestCounter = timeOutedRequestCounter;
        this.simulationTics = simulationTics;
        this.totalCost = totalCost;
        this.avgResponseTime = totalDelay / fulfilledRequestCounter;
        this.executionTime = executionTime;
        this.simulationSettings = SimSettingsExtractor.getSimulationSettings();
        this.allocationEvolution = allocationEvolution;
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

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
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

    public long getSimulationTics() {
        return simulationTics;
    }

    public void setSimulationTics(long simulationTics) {
        this.simulationTics = simulationTics;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public SimulationSettings getSimulationSettings() {
        return simulationSettings;
    }

    public void setSimulationSettings(SimulationSettings simulationSettings) {
        this.simulationSettings = simulationSettings;
    }

    public List<AllocationState> getAllocationEvolution() {
        return allocationEvolution;
    }

    public void setAllocationEvolution(List<AllocationState> allocationEvolution) {
        this.allocationEvolution = allocationEvolution;
    }
}
