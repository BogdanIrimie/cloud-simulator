package cloud.cluster.sim.controllersimulator;

import cloud.cluster.sim.clustersimulator.ClusterManager;
import cloud.cluster.sim.clustersimulator.FailureChanceComputer;
import cloud.cluster.sim.clustersimulator.dto.Time;
import cloud.cluster.sim.clustersimulator.dto.MicroDataCenter;
import cloud.cluster.sim.clustersimulator.dto.Vm;
import cloud.cluster.sim.utilities.MicroDataCentersExtractor;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

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
        greedyAllocation();
    }

    /**
     * Random allocation of VM when a scale up is done.
     */
    private void randomAllocation() {
        for (int i = 0; i < numberOfVmToAllocate; i++) {
            // random allocation of VM type
            List<MicroDataCenter> mDClist = new MicroDataCentersExtractor().extractMicroDataCenters();

            Random rand = new Random();
            int randomMicroDataCenterIndex = rand.nextInt(mDClist.size());

            clusterManager.addVm(new Vm(mDClist.get(randomMicroDataCenterIndex)));
        }
    }

    /**
     * Greedy allocation of VM types, the first VM that matches the desired availability
     * and has the lowest cost is allocated to the cluster when a scale up is done.
     */
    private void greedyAllocation() {
        for (int i = 0; i < numberOfVmToAllocate; i++) {

            // greedy allocation of VM types
            List<MicroDataCenter> mDClist = new MicroDataCentersExtractor().extractMicroDataCenters();
            mDClist.sort((a, b) -> a.compareTo(b));
            FailureChanceComputer fcc = new FailureChanceComputer();

            int mdcIndex = 0;
            double chanceFailAtTheSameTime = 1; // 100% percent chance
            while (chanceFailAtTheSameTime > 0.01 && mdcIndex < mDClist.size()) {
                chanceFailAtTheSameTime = fcc.failAtTheSameTime(clusterManager, mDClist.get(mdcIndex));
                mdcIndex++;
            }
            // assign value if change is ok or if we ran out of mdc types
            clusterManager.addVm(new Vm(mDClist.get(--mdcIndex)));
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
