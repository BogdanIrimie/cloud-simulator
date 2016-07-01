import cloudsimulator.clustersimulator.SimulateFailures;
import cloudsimulator.clustersimulator.dto.Cluster;
import cloudsimulator.clustersimulator.dto.TCGroup;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class TestSimulatedFailure {

    @Test
    public void testVmForFailure() {
        SimulateFailures simulateFailures = new SimulateFailures();
        double sla = 80.00;
        long vmCount = 1;
        Cluster cluster =
                new Cluster(
                        new LinkedList<TCGroup>(Arrays.asList(
                                new TCGroup("TC3", sla, vmCount))
                        )
                );


        long secondsToFailure = 0;
        while (cluster.getTgGroup().size() > 0) {
            simulateFailures.simulateFailures(cluster);
            secondsToFailure++;
        }

        System.out.println("Seconds until failure: " + secondsToFailure);
    }
}
