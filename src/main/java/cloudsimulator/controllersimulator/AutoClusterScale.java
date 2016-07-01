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

        // if the lower threshold is exceeded multiple times, increse the lowerThresholdExceedSuccession.
        if (requestInLastSecond < lowerThreshold) {
            lowerThresholdExceedSuccession++;
            lowerThresholdRequestsInLastSecconds += requestInLastSecond;
        }
        else {
            lowerThresholdExceedSuccession = 0;
            lowerThresholdRequestsInLastSecconds = 0;
        }

        // Decrease timeout if it is set.
        if (timeout > 0) {
            timeout--;
        }

        // Decide how many VMs should be allocated.
        if (upperThresholdExceedSuccession >= 3 && timeout == 0) {
            long numberOfVmToAllocate = computeNumberOfVmToAllocate(requestInLastSeconds / upperThresholdExceedSuccession, upperThreshold, rpsForOneVm);
            clusterFormationController.allocateVMs(numberOfVmToAllocate, clusterManager);

            timeout = 60;
            upperThresholdExceedSuccession = 0;
            requestInLastSeconds = 0;
        }

        // Decide how many VMs to remove
        if (lowerThresholdExceedSuccession >= 3 && timeout == 0) {
            long numberOfVmToRemove = computeNumberOfVmToRemove(requestInLastSeconds / lowerThresholdRequestsInLastSecconds, lowerThreshold, rpsForOneVm);
            clusterFormationController.removeVMs(numberOfVmToRemove, clusterManager);

            timeout = 60;
            lowerThresholdExceedSuccession = 0;
            lowerThresholdRequestsInLastSecconds = 0;
        }

    }

    // return the number of VMs that should be allocated to keep the resources utilisation < 70%.
    private long computeNumberOfVmToAllocate(long averageRequestRateInLastSeconds, long upperThreshold, long rpsForOneVm) {
        long utilisationForFutureAllocationOfVms = rpsForOneVm * upperUtilisationFactor / 100;
        long requestsOverTheThreshold = averageRequestRateInLastSeconds - upperThreshold;
        return requestsOverTheThreshold / utilisationForFutureAllocationOfVms + 1;
    }


    private long computeNumberOfVmToRemove(long averageRequestRateInLastSeconds, long lowerThreshold, long rpsForOneVm) {
        long utilisationForFutureVmDeallocation = rpsForOneVm * lowerUtilisationFactor / 100;
        long requestsUnderTheThreshold = lowerThreshold - averageRequestRateInLastSeconds;
        return requestsUnderTheThreshold / utilisationForFutureVmDeallocation + 1;
    }

}
