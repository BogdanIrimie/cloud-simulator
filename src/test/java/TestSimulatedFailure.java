import cloud.cluster.sim.clustersimulator.FailureInjector;
import cloud.cluster.sim.clustersimulator.dto.ClusterExtRep;
import cloud.cluster.sim.clustersimulator.dto.TCGroup;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class TestSimulatedFailure {

    @Test
    public void testVmForFailure() {
        // TODO needs refactoring in order to use new cluster.
//        FailureInjector failureInjector = new FailureInjector();
//        double sla = 80.00;
//        long vmCount = 1;
//        ClusterExtRep clusterExtRep =
//                new ClusterExtRep(
//                        new LinkedList<TCGroup>(Arrays.asList(
//                                new TCGroup("TC3", sla, vmCount))
//                        )
//                );
//
//
//        long secondsToFailure = 0;
//        while (clusterExtRep.getTgGroup().size() > 0) {
//            failureInjector.injectFailure(clusterExtRep);
//            secondsToFailure++;
//        }
//
//        System.out.println("Seconds until failure: " + secondsToFailure);
    }
}
