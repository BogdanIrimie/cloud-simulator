package cloud.cluster.sim.clustersimulator;

import cloud.cluster.sim.clustersimulator.dto.Vm;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Simulate failures for VMs. The lower is up time in SLA the higher the chance for a Vm to fail.
 */
@Component
public class FailureInjector {

    private static final int NUMBER_OF_DAYS_FOR_SLA = 30;
    private static final int NUMBER_OF_SECONDS_IN_DAY = 86400;          // 24 hours * 60 minutes * 60 seconds
    private static final int NUMBER_OF_OUTAGES = 98;

    private int totalNumberOfSeconds = NUMBER_OF_DAYS_FOR_SLA * NUMBER_OF_SECONDS_IN_DAY;


    /**
     * Simulate failures for all the machines in the cluster.
     *
     * @param clusterManager ClusterManager for which failures will be simulated.
     * @return ClusterManager after failure simulation.
     */
    public void injectFailure(ClusterManager clusterManager) {
        for (int i = 0; i < clusterManager.getClusterSize(); i++) {
            Vm vm = clusterManager.getCluster().getVms().get(i);
            if (testVmFarFailure(vm.getMicroDataCenter().getSla())) {
                clusterManager.failVm(i);
            }
        }
    }

    /**
     * Test a particular VM in the cluster for failure.
     *
     * @param sla specifying the up time for a VM.
     * @return boolean value indicating the the machine has failed or not.
     */
    private boolean testVmFarFailure(double sla) {
        long downtime;
        double mttr, failures;

        downtime = (long) (totalNumberOfSeconds *  (100 - sla)) / 100;
        mttr = downtime / NUMBER_OF_OUTAGES;
        failures = downtime / mttr;

        Random random = new Random();
        int randomInt = random.nextInt(totalNumberOfSeconds);

        if (randomInt <= failures) {
            return true;
        }
        return false;
    }

}
