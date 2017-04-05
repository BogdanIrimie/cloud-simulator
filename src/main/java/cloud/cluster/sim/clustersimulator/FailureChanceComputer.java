package cloud.cluster.sim.clustersimulator;

import cloud.cluster.sim.clustersimulator.dto.MicroDataCenter;


public class FailureChanceComputer {

    public double failAtTheSameTime(ClusterManager clusterManager, MicroDataCenter mDC) {

        // compute chance for the cluster
        double chanceFailAtTheSameTime = clusterManager.getCluster().getVms().stream()
                .mapToDouble(clusterVm ->
                        new FailureChance().compute(clusterVm.getMicroDataCenter().getSla()).getChance())
                .reduce(1, (a,b) -> a * b );

        // add the new VM that might be added to the cluster
        chanceFailAtTheSameTime *= new FailureChance().compute(mDC.getSla()).getChance();

        return chanceFailAtTheSameTime;
    }
}
