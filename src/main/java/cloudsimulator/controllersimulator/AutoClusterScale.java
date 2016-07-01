package cloudsimulator.controllersimulator;

import cloudsimulator.clustersimulator.ClusterManager;
import org.springframework.stereotype.Component;

/**
 * Implements auto scaling policies for the Cluster.
 */
@Component
public class AutoClusterScale {

    private int utilisationFactor = 70;
    private int timeout = 0;
    private int succession = 0;
    private long requestInLastSeconds;

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

        long upperThreshold = utilisationFactor * maxRps / 100;

        // If the upperThreshold is exceeded multiple times in a row, the succession will increment.
        if (requestInLastSecond > upperThreshold) {
            succession++;
            requestInLastSeconds += requestInLastSecond;
        }
        else {
            succession = 0;
            requestInLastSeconds = 0;
        }

        // Decrease timeout if it is set.
        if (timeout > 0) {
            timeout--;
        }

        // Decide how many VMs should be allocated.
        if (succession >= 3 && timeout == 0) {
            long numberOfVmToAllocate = computeNumberOfVmToAllocate(requestInLastSeconds / succession, upperThreshold, rpsForOneVm);

            ClusterFormationController clusterFormationController = new ClusterFormationController();
            clusterFormationController.allocateVMs(numberOfVmToAllocate, clusterManager);

            timeout = 60;
            requestInLastSeconds = 0;
        }

    }

    // return the number of VMs that should be allocated to keep the resources utilisation < 70%.
    private long computeNumberOfVmToAllocate(long averageRequestRateInLastSeconds, long upperThreshold, long rpsForOneVm) {
        long requestsOverTheThreshold = averageRequestRateInLastSeconds - upperThreshold;
        return requestsOverTheThreshold / rpsForOneVm + 1;
    }

}
