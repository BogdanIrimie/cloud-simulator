package cloud.cluster.sim.utilities.dto;

public class SimulationSettings {
    private int taskTimeout = 5;
    private long startVmDelay = 45;
    private long stopVmDelay = 15;
    private int vmCreationTimeout = 60;
    private int vmTerminationTimeout = 25;
    private int lowUtilisationThreshold = 50;
    private int upUtilisationThreshold = 70;
    private int lowThresholdExceedSuccessionLimit = 21;
    private int upThresholdExceedSuccessionLimit = 11;
    private long opsForVm = 100;
    private int mttr = 120;
    private String pathToTraces = "traces";
    private String regexForTracesName = "trimmed";

    public SimulationSettings() {
    }

    public SimulationSettings(int taskTimeout, long startVmDelay, long stopVmDelay, int vmCreationTimeout, int vmTerminationTimeout, int lowUtilisationThreshold, int upUtilisationThreshold, int lowThresholdExceedSuccessionLimit, int upThresholdExceedSuccessionLimit, long opsForVm, int mttr, String pathToTraces, String regexForTracesName) {
        this.taskTimeout = taskTimeout;
        this.startVmDelay = startVmDelay;
        this.stopVmDelay = stopVmDelay;
        this.vmCreationTimeout = vmCreationTimeout;
        this.vmTerminationTimeout = vmTerminationTimeout;
        this.lowUtilisationThreshold = lowUtilisationThreshold;
        this.upUtilisationThreshold = upUtilisationThreshold;
        this.lowThresholdExceedSuccessionLimit = lowThresholdExceedSuccessionLimit;
        this.upThresholdExceedSuccessionLimit = upThresholdExceedSuccessionLimit;
        this.opsForVm = opsForVm;
        this.mttr = mttr;
        this.pathToTraces = pathToTraces;
        this.regexForTracesName = regexForTracesName;
    }

    public int getTaskTimeout() {
        return taskTimeout;
    }

    public void setTaskTimeout(int taskTimeout) {
        this.taskTimeout = taskTimeout;
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

    public int getLowUtilisationThreshold() {
        return lowUtilisationThreshold;
    }

    public void setLowUtilisationThreshold(int lowUtilisationThreshold) {
        this.lowUtilisationThreshold = lowUtilisationThreshold;
    }

    public int getUpUtilisationThreshold() {
        return upUtilisationThreshold;
    }

    public void setUpUtilisationThreshold(int upUtilisationThreshold) {
        this.upUtilisationThreshold = upUtilisationThreshold;
    }

    public int getLowThresholdExceedSuccessionLimit() {
        return lowThresholdExceedSuccessionLimit;
    }

    public void setLowThresholdExceedSuccessionLimit(int lowThresholdExceedSuccessionLimit) {
        this.lowThresholdExceedSuccessionLimit = lowThresholdExceedSuccessionLimit;
    }

    public int getUpThresholdExceedSuccessionLimit() {
        return upThresholdExceedSuccessionLimit;
    }

    public void setUpThresholdExceedSuccessionLimit(int upThresholdExceedSuccessionLimit) {
        this.upThresholdExceedSuccessionLimit = upThresholdExceedSuccessionLimit;
    }

    public long getOpsForVm() {
        return opsForVm;
    }

    public void setOpsForVm(long opsForVm) {
        this.opsForVm = opsForVm;
    }

    public int getMttr() {
        return mttr;
    }

    public void setMttr(int mttr) {
        this.mttr = mttr;
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
