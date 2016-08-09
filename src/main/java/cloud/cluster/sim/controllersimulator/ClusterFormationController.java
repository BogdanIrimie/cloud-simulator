package cloud.cluster.sim.controllersimulator;

import cloud.cluster.sim.clustersimulator.ClusterManager;
import cloud.cluster.sim.clustersimulator.dto.Time;
import cloud.cluster.sim.clustersimulator.dto.MicroDataCenter;
import cloud.cluster.sim.clustersimulator.dto.Vm;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import org.springframework.stereotype.Component;

/**
 * Take decisions regarding cluster formation.
 */
@Component
public class ClusterFormationController {

    //private long time = 0;
    private double startTimeOfAllocation = -1;
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
        startTimeOfAllocation = Time.simulationTime - (Time.simulationTime - Time.logTime > 0 ?
                Time.simulationTime - Time.logTime : 0);
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
        this.clusterManager = clusterManager;
        this.numberOfVmToRemove = numberOfVmToRemove;
        remove();
    }

    /**
     * Simulate passing of time in order to start or stop a VM.
     */
    public void signalTimeIncrement() {
        if (startTimeOfAllocation != -1 && Time.simulationTime >=  startTimeOfAllocation + SimSettingsExtractor.getSimulationSettings().getStartVmDelay()) {
            allocate();
            startTimeOfAllocation = -1;
        }
    }

    /**
     * After a VM is started, it is allocated to the cluster.
     */
    private void allocate () {
        for (int i = 0; i < numberOfVmToAllocate; i++) {
            clusterManager.addVm(new Vm(new MicroDataCenter("TC1", 99.99, 3)));
        }
    }

    /**
     * After a VM is stopped, it is removed from the cluster.
     */
    private void remove() {
        for (int i = 0; i < numberOfVmToRemove; i++) {
            clusterManager.removeVm(0);
        }
    }

}
