package cloudsimulator.clustersimulator;

import cloudsimulator.clustersimulator.dto.SimulatedCluster;

import java.util.Random;

public class SimulateFailures {

    public SimulatedCluster simulateFailures(SimulatedCluster simulatedCluster) {

        simulatedCluster.getCluster().forEach(tcg -> {
            tcg.getSla();
        });

        return simulatedCluster;
    }

    public static void main(String[] args) {
        SimulateFailures simulateFailures = new SimulateFailures();
        int failedVmCount = 0;

        for (int i = 0; i < 86400 * 30; i++) {
            boolean isVmFailed = simulateFailures.testVmFarFailure(99.99);
            //System.out.println("Is vm available: " + isVmFailed);
            if (isVmFailed) {
                failedVmCount++;
            }
        }
        System.out.println("Failed VMs: " + failedVmCount);
    }

    private boolean testVmFarFailure(double sla) {
        int numberOfDaysForSla = 30;
        int seconds;                                                                      // 24 hours * 60 minutes * 60 seconds;
        long secondsOffline;

        seconds = numberOfDaysForSla * 86400;
        secondsOffline = (long) (seconds *  (100 - sla)) / 100;

        Random random = new Random();
        int randomInt = random.nextInt(seconds);
        //System.out.print(randomInt + " ");
        if (randomInt <= (secondsOffline / 60)) {
            return true;
        }
        return false;
    }
}
