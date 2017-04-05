package cloud.cluster.sim.clustersimulator;

import cloud.cluster.sim.clustersimulator.dto.Vm;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import org.springframework.stereotype.Component;

@Component
public class CostComputer {

    private long totalCost = 0;

    public long getTotalCost() {
        return totalCost;
    }

    public void addCostForLastSecond(ClusterManager clusterManager) {
        totalCost += clusterManager.getCluster().getVms().stream()
                .mapToLong(vm -> vm.getMicroDataCenter().getCost())
                .sum();
    }

    public void addCostForShutDown(Vm vm) {
        totalCost += SimSettingsExtractor.getSimulationSettings().getStopVmDelay() * vm.getMicroDataCenter().getCost();
    }

}
