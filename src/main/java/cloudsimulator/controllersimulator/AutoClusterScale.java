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
        }
        else {
            succession = 0;
        }

        // Decrease timeout if it is set.
        if (timeout > 0) {
            timeout--;
        }

        // Decide how many VMs should be allocated.
        if (succession >= 3 && timeout == 0) {
            long numberOfVmToAllocate = computeNumberOfVmToAllocate(requestInLastSecond, upperThreshold, rpsForOneVm);
            timeout = 60;
        }

    }

    private long computeNumberOfVmToAllocate(long requestInLastSecond, long threshold, long rpsForOneVm) {
        return 0;
    }

}
