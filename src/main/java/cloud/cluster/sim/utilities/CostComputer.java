package cloud.cluster.sim.utilities;

import cloud.cluster.sim.clustersimulator.ClusterManager;
import org.springframework.stereotype.Component;

@Component
public class CostComputer {

    private long totalCost = 0;

    public long getTotalCost() {
        return totalCost;
    }

    public void addCostForLastSecond(ClusterManager clusterManager) {
        totalCost += clusterManager.getCluster().getTgGroup().stream()
                .mapToLong(tcGroup-> tcGroup.getCost() * tcGroup.getVmNumber())
                .sum();
    }

}
