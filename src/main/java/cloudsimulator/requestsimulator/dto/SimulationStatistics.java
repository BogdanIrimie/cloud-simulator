package cloudsimulator.requestsimulator.dto;

public class SimulationStatistics {

    private double totalDelay = 0;
    private long totalRequestCounter = 0, fulfilledRequestCounter = 0, timeOutedRequestCounter = 0;

    public SimulationStatistics() {
    }

    public SimulationStatistics(double totalDelay, long totalRequestCounter, long fulfilledRequestCounter, long timeOutedRequestCounter) {
        this.totalDelay = totalDelay;
        this.totalRequestCounter = totalRequestCounter;
        this.fulfilledRequestCounter = fulfilledRequestCounter;
        this.timeOutedRequestCounter = timeOutedRequestCounter;
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
}
