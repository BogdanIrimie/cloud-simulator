package cloudsimulator.utilities.dto;

public class SimulationSettings {
    private long startVmDelay = 40;
    private long stopVmDelay = 15;
    private int vmCreationTimeout = 60;
    private int vmTerminationTimeout = 60;
    private int lowerUtilisationThreshold = 50;
    private int upperUtilisationThreshold = 70;
    private int lowerThresholdExceedSuccessionLimit = 3;
    private int upperThresholdExceedSuccessionLimit = 3;
    private long rpsForVm = 100;
    private String pathToTraces = "traces";
    private String regexForTracesName = "trimmed";

    public SimulationSettings() {
    }

    public SimulationSettings(long startVmDelay, long stopVmDelay, int vmCreationTimeout, int vmTerminationTimeout, int lowerUtilisationThreshold, int upperUtilisationThreshold, int lowerThresholdExceedSuccessionLimit, int upperThresholdExceedSuccessionLimit, long rpsForVm, String pathToTraces, String regexForTracesName) {
        this.startVmDelay = startVmDelay;
        this.stopVmDelay = stopVmDelay;
        this.vmCreationTimeout = vmCreationTimeout;
        this.vmTerminationTimeout = vmTerminationTimeout;
        this.lowerUtilisationThreshold = lowerUtilisationThreshold;
        this.upperUtilisationThreshold = upperUtilisationThreshold;
        this.lowerThresholdExceedSuccessionLimit = lowerThresholdExceedSuccessionLimit;
        this.upperThresholdExceedSuccessionLimit = upperThresholdExceedSuccessionLimit;
        this.rpsForVm = rpsForVm;
        this.pathToTraces = pathToTraces;
        this.regexForTracesName = regexForTracesName;
    }

    public long getStartVmDelay() {
        return startVmDelay;
    }

    public void setStartVmDelay(long startVmDelay) {
        this.startVmDelay = startVmDelay;
    }

    public long getStopVmDelay() {
        return stopVmDelay;
    }

    public void setStopVmDelay(long stopVmDelay) {
        this.stopVmDelay = stopVmDelay;
    }

    public int getVmCreationTimeout() {
        return vmCreationTimeout;
    }

    public void setVmCreationTimeout(int vmCreationTimeout) {
        this.vmCreationTimeout = vmCreationTimeout;
    }

    public int getVmTerminationTimeout() {
        return vmTerminationTimeout;
    }

    public void setVmTerminationTimeout(int vmTerminationTimeout) {
        this.vmTerminationTimeout = vmTerminationTimeout;
    }

    public int getLowerUtilisationThreshold() {
        return lowerUtilisationThreshold;
    }

    public void setLowerUtilisationThreshold(int lowerUtilisationThreshold) {
        this.lowerUtilisationThreshold = lowerUtilisationThreshold;
    }

    public int getUpperUtilisationThreshold() {
        return upperUtilisationThreshold;
    }

    public void setUpperUtilisationThreshold(int upperUtilisationThreshold) {
        this.upperUtilisationThreshold = upperUtilisationThreshold;
    }

    public int getLowerThresholdExceedSuccessionLimit() {
        return lowerThresholdExceedSuccessionLimit;
    }

    public void setLowerThresholdExceedSuccessionLimit(int lowerThresholdExceedSuccessionLimit) {
        this.lowerThresholdExceedSuccessionLimit = lowerThresholdExceedSuccessionLimit;
    }

    public int getUpperThresholdExceedSuccessionLimit() {
        return upperThresholdExceedSuccessionLimit;
    }

    public void setUpperThresholdExceedSuccessionLimit(int upperThresholdExceedSuccessionLimit) {
        this.upperThresholdExceedSuccessionLimit = upperThresholdExceedSuccessionLimit;
    }

    public long getRpsForVm() {
        return rpsForVm;
    }

    public void setRpsForVm(long rpsForVm) {
        this.rpsForVm = rpsForVm;
    }

    public String getPathToTraces() {
        return pathToTraces;
    }

    public void setPathToTraces(String pathToTraces) {
        this.pathToTraces = pathToTraces;
    }

    public String getRegexForTracesName() {
        return regexForTracesName;
    }

    public void setRegexForTracesName(String regexForTracesName) {
        this.regexForTracesName = regexForTracesName;
    }

}
