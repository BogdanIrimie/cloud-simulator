package cloudsimulator.controllersimulator;

import cloudsimulator.clustersimulator.ClusterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implements auto scaling policies for the Cluster.
 */
@Component
public class AutoClusterScale {

    @Autowired
    ClusterFormationController clusterFormationController;

    private int upperUtilisationFactor = 70;
    private int lowerUtilisationFactor = 60;
    private int timeout = 0;
    private int upperThresholdExceedSuccession = 0;
    private int lowerThresholdExceedSuccession = 0;
    private long requestInLastSeconds;
    private long lowerThresholdRequestsInLastSecconds;

    /**
     * Decide to scale when the threshold is exceeded for multiple times in a row
     * and when there is no timeout from the last decision to scale.
     *
     * @param clusterManager
     * @param requestInLastSecond
     */
    public void scalePolicy(ClusterManager clusterManager, long requestInLastSecond) {
        long rpsForOneVm  = clusterManager.getRpsForOneVm();
        long maxRps =  clusterManager.computeMaxRps();

        long upperThreshold = upperUtilisationFactor * maxRps / 100;
        long lowerThreshold = lowerUtilisationFactor * maxRps / 100;

        // If the upperThreshold is exceeded multiple times in a row, the upperThresholdExceedSuccession will increment.
        if (requestInLastSecond > upperThreshold) {
            upperThresholdExceedSuccession++;
            requestInLastSeconds += requestInLastSecond;
        }
        else {
            upperThresholdExceedSuccession = 0;
            requestInLastSeconds = 0;
        }

        // if the lower threshold is exceeded multiple times, increase the lowerThresholdExceedSuccession.
        if (requestInLastSecond < lowerThreshold) {
            lowerThresholdExceedSuccession++;
            lowerThresholdRequestsInLastSecconds += requestInLastSecond;
        }
        else {
            lowerThresholdExceedSuccession = 0;
            lowerThresholdRequestsInLastSecconds = 0;
        }

        if (timeout > 0) {
            timeout--;
        }

        // Allocate VMs if the upper threshold was reached several consecutive times.
        if (upperThresholdExceedSuccession >= 3 && timeout == 0) {
            long numberOfVmToAllocate = computeNumberOfVmToAllocate(requestInLastSeconds / upperThresholdExceedSuccession, upperThreshold, rpsForOneVm);
            clusterFormationController.allocateVMs(numberOfVmToAllocate, clusterManager);

            timeout = 60;
            upperThresholdExceedSuccession = 0;
            requestInLastSeconds = 0;
        }

        // Remove VMs if the lower threshold was reached several times.
        if (lowerThresholdExceedSuccession >= 3 && timeout == 0) {
            long numberOfVmToRemove = computeNumberOfVmToRemove(requestInLastSeconds / lowerThresholdRequestsInLastSecconds, lowerThreshold, rpsForOneVm);
            clusterFormationController.removeVMs(numberOfVmToRemove, clusterManager);

            timeout = 60;
            lowerThresholdExceedSuccession = 0;
            lowerThresholdRequestsInLastSecconds = 0;
        }

    }

    /**
     * Compute number of VMs that should be allocated to keep resources utilisation bellow the upper threshold.
     *
     * @param averageRequestRateInLastSeconds the average request rate in the last seconds.
     * @param upperThreshold is used to trigger VM allocation.
     * @param rpsForOneVm the maximum number of requests per second that a VM can handle.
     * @return the number of VMs that should be allocated.
     */
    private long computeNumberOfVmToAllocate(long averageRequestRateInLastSeconds, long upperThreshold, long rpsForOneVm) {
        long utilisationForFutureAllocationOfVms = rpsForOneVm * upperUtilisationFactor / 100;
        long requestsOverTheThreshold = averageRequestRateInLastSeconds - upperThreshold;
        return requestsOverTheThreshold / utilisationForFutureAllocationOfVms + 1;
    }


    /**
     * Compute number of VM that should be stopped because the lower threshold was reached.
     *
     * @param averageRequestRateInLastSeconds the average request rate in the last seconds.
     * @param lowerThreshold is used to trigger removal of VMs.
     * @param rpsForOneVm the maximum number of requests per second that a VM can handle.
     * @return number of VMs that should be removed from the cluster.
     */
    private long computeNumberOfVmToRemove(long averageRequestRateInLastSeconds, long lowerThreshold, long rpsForOneVm) {
        long utilisationForFutureVmDeallocation = rpsForOneVm * lowerUtilisationFactor / 100;
        long requestsUnderTheThreshold = lowerThreshold - averageRequestRateInLastSeconds;
        return requestsUnderTheThreshold / utilisationForFutureVmDeallocation + 1;
    }

}
