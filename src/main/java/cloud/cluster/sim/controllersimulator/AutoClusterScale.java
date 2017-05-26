package cloud.cluster.sim.controllersimulator;

import cloud.cluster.sim.clustersimulator.ClusterManager;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implements auto scaling policies for the Cluster.
 */
@Component
public class AutoClusterScale {

    @Autowired
    ClusterFormationController clusterFormationController;

    private int upperUtilisationFactor;
    private int lowerUtilisationFactor;
    private int timeout;
    private int upperExceedSuccessionLimit;
    private int lowerExceedSuccessionLimit;
    private int upperThresholdExceedSuccessionLimit;
    private int lowerThresholdExceedSuccessionLimit;
    private int vmCreationTimeout;
    private int vmTerminationTimeout;
    private long upperRequestsInLastSeconds;
    private long lowerRequestsInLastSeconds;

    public AutoClusterScale() {
        this.upperUtilisationFactor = SimSettingsExtractor.getSimulationSettings().getUpUtilisationThreshold();
        this.lowerUtilisationFactor = SimSettingsExtractor.getSimulationSettings().getLowUtilisationThreshold();
        this.upperThresholdExceedSuccessionLimit = SimSettingsExtractor.getSimulationSettings().getUpThresholdExceedSuccessionLimit();
        this.lowerThresholdExceedSuccessionLimit = SimSettingsExtractor.getSimulationSettings().getLowThresholdExceedSuccessionLimit();
        this.vmCreationTimeout = SimSettingsExtractor.getSimulationSettings().getVmCreationTimeout();
        this.vmTerminationTimeout = SimSettingsExtractor.getSimulationSettings().getVmTerminationTimeout();
    }

    /**
     * Decide to scale when the threshold is exceeded for multiple times in a row
     * and when there is no timeout from the last decision to scale.
     *
     * @param clusterManager allows operations on cluster.
     * @param requestInLastSecond number of requests received in the last second.
     */
    public void scalePolicy(ClusterManager clusterManager, long requestInLastSecond) {
        long opsForOneVm  = clusterManager.getOpsForOneVm();
        long maxOps =  clusterManager.computeCumulativeOpsForCluster();

        long upperThreshold = upperUtilisationFactor * maxOps / 100;
        long lowerThreshold = lowerUtilisationFactor * maxOps / 100;

        // If the upperThreshold is exceeded multiple times in a row, the upperExceedSuccessionLimit will increment.
        if (requestInLastSecond > upperThreshold) {
            upperExceedSuccessionLimit++;
            upperRequestsInLastSeconds += requestInLastSecond;
        }
        else {
            upperExceedSuccessionLimit = 0;
            upperRequestsInLastSeconds = 0;
        }

        // if the lower threshold is exceeded multiple times, increase the lowerExceedSuccessionLimit.
        if (requestInLastSecond < lowerThreshold && clusterManager.getClusterSize() > 1) {
            lowerExceedSuccessionLimit++;
            lowerRequestsInLastSeconds += requestInLastSecond;
        }
        else {
            lowerExceedSuccessionLimit = 0;
            lowerRequestsInLastSeconds = 0;
        }

        if (timeout > 0) {
            timeout--;
        }

        // Allocate VMs if the upper threshold was reached several consecutive times.
        if (upperExceedSuccessionLimit >= upperThresholdExceedSuccessionLimit && timeout == 0) {
            long numberOfVmToAllocate = computeNumberOfVmToAllocate(upperRequestsInLastSeconds / upperExceedSuccessionLimit, upperThreshold, opsForOneVm);
            clusterFormationController.allocateVMs(numberOfVmToAllocate, clusterManager);

            timeout = vmCreationTimeout;
            upperExceedSuccessionLimit = 0;
            upperRequestsInLastSeconds = 0;
        }

        // Remove VMs if the lower threshold was reached several times.
        if (lowerExceedSuccessionLimit >= lowerThresholdExceedSuccessionLimit && timeout == 0) {
            long numberOfVmToRemove = computeNumberOfVmToRemove(lowerRequestsInLastSeconds / lowerExceedSuccessionLimit, lowerThreshold, opsForOneVm);
            clusterFormationController.removeVMs(numberOfVmToRemove, clusterManager);

            timeout = vmTerminationTimeout;
            lowerExceedSuccessionLimit = 0;
            lowerRequestsInLastSeconds = 0;
        }

        clusterFormationController.signalTimeIncrement();
    }

    /**
     * Compute number of VMs that should be allocated to keep resources utilisation bellow the upper threshold.
     *
     * @param averageRequestRateInLastSeconds the average request rate in the last seconds.
     * @param upperThreshold is used to trigger VM allocation.
     * @param opsForOneVm the maximum number of requests per second that a VM can handle.
     * @return the number of VMs that should be allocated.
     */
    private long computeNumberOfVmToAllocate(long averageRequestRateInLastSeconds, long upperThreshold, long opsForOneVm) {
        long utilisationForFutureAllocationOfVms = opsForOneVm * upperUtilisationFactor / 100;
        long requestsOverTheThreshold = averageRequestRateInLastSeconds - upperThreshold;
        return requestsOverTheThreshold / utilisationForFutureAllocationOfVms + 1;
    }


    /**
     * Compute number of VM that should be stopped because the lower threshold was reached.
     *
     * @param averageRequestRateInLastSeconds the average request rate in the last seconds.
     * @param lowerThreshold is used to trigger removal of VMs.
     * @param opsForOneVm the maximum number of requests per second that a VM can handle.
     * @return number of VMs that should be removed from the cluster.
     */
    private long computeNumberOfVmToRemove(long averageRequestRateInLastSeconds, long lowerThreshold, long opsForOneVm) {
        long utilisationForFutureVmDeallocation = opsForOneVm * lowerUtilisationFactor / 100;
        long requestsUnderTheThreshold = lowerThreshold - averageRequestRateInLastSeconds;
        return requestsUnderTheThreshold / utilisationForFutureVmDeallocation + 1;
    }

}
