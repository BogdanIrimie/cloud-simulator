package cloudsimulator.controllersimulator;

import cloudsimulator.clustersimulator.ClusterManager;
import org.springframework.stereotype.Component;

/**
 * Take decisions regarding cluster formation.
 */
@Component
public class ClusterFormationController {

    private long time = 0;
    private long startTimeOfAllocation = -1, startTimeOfRemoval = -1;
    private ClusterManager clusterManager;
    private long numberOfVmToAllocate;
    private long numberOfVmToRemove;

    /**
     * Mark VMs for allocation, but some time is required before they are booted up.
     *
     * @param numberOfVmToAllocate
     * @param clusterManager
     */
    public void allocateVMs(long numberOfVmToAllocate, ClusterManager clusterManager) {
        startTimeOfAllocation = time;
        this.clusterManager = clusterManager;
        this.numberOfVmToAllocate = numberOfVmToAllocate;
    }

    /**
     * Mark VMs for removal, but some time is required before they are shut down.
     *
     * @param numberOfVmToRemove
     * @param clusterManager
     */
    public void removeVMs(long numberOfVmToRemove, ClusterManager clusterManager) {
        startTimeOfRemoval = time;
        this.clusterManager = clusterManager;
        this.numberOfVmToRemove = numberOfVmToRemove;
    }

    /**
     * Simulate passing of time in order to start or stop a VM.
     */
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

    /**
     * After a VM is started, it is allocated to the cluster.
     */
    private void allocate () {
        clusterManager.getCluster().getTgGroup().stream()
                .findFirst()
                .ifPresent(tgGroup -> {
                    tgGroup.setVmNumber(tgGroup.getVmNumber() + numberOfVmToAllocate);
                });
    }

    /**
     * After a VM is stoped, it is removed from the cluster.
     */
    private void remove() {
        clusterManager.getCluster().getTgGroup().stream()
                .findFirst()
                .ifPresent(tgGroup -> {
                    tgGroup.setVmNumber(tgGroup.getVmNumber() - numberOfVmToRemove);
                });
    }

}
