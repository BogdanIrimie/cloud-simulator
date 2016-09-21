package cloud.cluster.sim.clustersimulator;

import cloud.cluster.sim.clustersimulator.dto.Chance;
import cloud.cluster.sim.clustersimulator.dto.Vm;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Simulate failures for VMs. The lower is up time in SLA the higher the chance for a Vm to fail.
 */
@Component
public class FailureInjector {

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

        FailureChance fc = new FailureChance();
        Chance chance = fc.compute(sla);

        Random random = new Random();
        int randomInt = random.nextInt(chance.getPossible());

        if (randomInt <= chance.getFavorable()) {
            return true;
        }
        return false;
    }

}
