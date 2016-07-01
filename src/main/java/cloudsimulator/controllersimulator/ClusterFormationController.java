package cloudsimulator.controllersimulator;

import cloudsimulator.clustersimulator.ClusterManager;
import org.springframework.stereotype.Component;

@Component
public class ClusterFormationController {

    private long time = 0;
    private long startTimeOfAllocation = -1, startTimeOfRemoval = -1;
    private ClusterManager clusterManager;
    private long numberOfVmToAllocate;
    private long numberOfVmToRemove;

    public void allocateVMs(long numberOfVmToAllocate, ClusterManager clusterManager) {
        startTimeOfAllocation = time;
        this.clusterManager = clusterManager;
        this.numberOfVmToAllocate = numberOfVmToAllocate;
    }


    public void removeVMs(long numberOfVmToRemove, ClusterManager clusterManager) {
        startTimeOfRemoval = time;
        this.clusterManager = clusterManager;
        this.numberOfVmToRemove = numberOfVmToRemove;
    }

    public void incrementTime() {
        time++;
        if (startTimeOfAllocation != -1 && time >=  startTimeOfAllocation + 40) {
            allocate();
            startTimeOfAllocation = -1;
        }
        if (startTimeOfRemoval != -1 && time >= startTimeOfRemoval + 15) {
            remove();
            startTimeOfRemoval = -1;
        }
    }

    private void allocate () {
        clusterManager.getCluster().getTgGroup().stream()
                .findFirst()
                .ifPresent(tgGroup -> {
                    tgGroup.setVmNumber(tgGroup.getVmNumber() + numberOfVmToAllocate);
                });
    }

    private void remove() {
        clusterManager.getCluster().getTgGroup().stream()
                .findFirst()
                .ifPresent(tgGroup -> {
                    tgGroup.setVmNumber(tgGroup.getVmNumber() - numberOfVmToRemove);
                });
    }
}
