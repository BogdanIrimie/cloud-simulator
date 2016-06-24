package cloudsimulator.clustersimulator;

import cloudsimulator.clustersimulator.dto.SimulatedCluster;

import java.util.Random;

/**
 * Simulate failures for VMs. The lower is up time in SLA the higher the chance for a Vm to fail.
 */
public class SimulateFailures {

    private static final int NUMBER_OF_DAYS_FOR_SLA = 30;
    private static final int NUMBER_OF_SECONDS_IN_DAY = 86400;          // 24 hours * 60 minutes * 60 seconds

    /**
     * Simulate failures for all the machines in the cluster.
     *
     * @param simulatedCluster Cluster for which failures will be simulated.
     * @return Cluster after failure simulation.
     */
    public SimulatedCluster simulateFailures(SimulatedCluster simulatedCluster) {

        simulatedCluster.getCluster().forEach(tcg -> {
            // Test each machine in the treatment category.
            for (int i = 0; i < tcg.getVmNumber(); i++) {
                if (testVmFarFailure(tcg.getSla())) {
                    tcg.setVmNumber(tcg.getVmNumber() - 1);
                }
            }
        });

        // Remove a treatment category if there are no VMs in it.
        simulatedCluster.getCluster().removeIf(tcg -> tcg.getVmNumber() == 0);

        return simulatedCluster;
    }

    /**
     * Test a particular VM in the cluster for failure.
     *
     * @param sla specifying the uptime for a VM.
     * @return boolean value indicating the the machine has failed or not.
     */
    private boolean testVmFarFailure(double sla) {
        int seconds;
        long secondsOffline;

        seconds = NUMBER_OF_DAYS_FOR_SLA * NUMBER_OF_SECONDS_IN_DAY;
        secondsOffline = (long) (seconds *  (100 - sla)) / 100;

        Random random = new Random();
        int randomInt = random.nextInt(seconds);
        long numberOfRestart = secondsOffline / 60;

        if (randomInt <= (secondsOffline / 60)) {
            return true;
        }
        return false;
    }

}
