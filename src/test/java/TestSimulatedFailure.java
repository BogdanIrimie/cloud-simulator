import org.junit.Test;

public class TestSimulatedFailure {

    @Test
    public void testVmForFailure() {
        // TODO needs refactoring in order to use new cluster.
//        FailureInjector failureInjector = new FailureInjector();
//        double sla = 80.00;
//        long vmCount = 1;
//        ClusterExtRep clusterExtRep =
//                new ClusterExtRep(
//                        new LinkedList<MdcGroup>(Arrays.asList(
//                                new MdcGroup("TC3", sla, vmCount))
//                        )
//                );
//
//
//        long secondsToFailure = 0;
//        while (clusterExtRep.getMdcGroup().size() > 0) {
//            failureInjector.injectFailure(clusterExtRep);
//            secondsToFailure++;
//        }
//
//        System.out.println("Seconds until failure: " + secondsToFailure);
    }
}
