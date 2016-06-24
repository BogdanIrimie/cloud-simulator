import cloudsimulator.clustersimulator.SimulateFailures;
import cloudsimulator.clustersimulator.dto.SimulatedCluster;
import cloudsimulator.clustersimulator.dto.TCGroup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.LinkedList;

public class TestSimulatedFailure {

    @Test
    public void testVmForFailure() {
        SimulateFailures simulateFailures = new SimulateFailures();
        double sla = 80.00;
        long vmCount = 1;
        SimulatedCluster simulatedCluster =
                new SimulatedCluster(
                        new LinkedList<TCGroup>(Arrays.asList(
                                new TCGroup("TC3", sla, vmCount))
                        )
                );


        long secondsToFailure = 0;
        while (simulatedCluster.getCluster().size() > 0) {
            simulateFailures.simulateFailures(simulatedCluster);
            secondsToFailure++;
        }

        System.out.println("Seconds until failure: " + secondsToFailure);
    }
}
