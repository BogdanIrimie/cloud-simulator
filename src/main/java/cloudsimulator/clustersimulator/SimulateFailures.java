package cloudsimulator.clustersimulator;

import cloudsimulator.clustersimulator.dto.SimulatedCluster;

import java.util.Random;

public class SimulateFailures {

    private static final int numberOfDaysForSla = 30;

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

    private boolean testVmFarFailure(double sla) {
        int seconds;                                                                      // 24 hours * 60 minutes * 60 seconds;
        long secondsOffline;

        seconds = numberOfDaysForSla * 86400;
        secondsOffline = (long) (seconds *  (100 - sla)) / 100;

        Random random = new Random();
        int randomInt = random.nextInt(seconds);
        long numberOfRestart = secondsOffline / 60;

        if (randomInt <= (secondsOffline / 60)) {
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        SimulateFailures simulateFailures = new SimulateFailures();
        int failedVmCount = 0;

        for (int i = 0; i < 86400 * numberOfDaysForSla; i++) {
            boolean isVmFailed = simulateFailures.testVmFarFailure(80.00);
            //System.out.println("Is vm available: " + isVmFailed);
            if (isVmFailed) {
                failedVmCount++;
            }
        }
        System.out.println("Failed VMs: " + failedVmCount);
    }
}
