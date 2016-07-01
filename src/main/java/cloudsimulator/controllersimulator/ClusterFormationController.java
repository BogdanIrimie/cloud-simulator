package cloudsimulator.controllersimulator;

import cloudsimulator.clustersimulator.ClusterManager;
import org.springframework.stereotype.Component;

@Component
public class ClusterFormationController {


    public void allocateVMs(long numberOfVmToAllocate, ClusterManager clusterManager) {
        clusterManager.getCluster().getTgGroup().stream()
                .findFirst()
                .ifPresent(tgGroup -> {
                    tgGroup.setVmNumber(tgGroup.getVmNumber() + numberOfVmToAllocate);
                });
    }


}
