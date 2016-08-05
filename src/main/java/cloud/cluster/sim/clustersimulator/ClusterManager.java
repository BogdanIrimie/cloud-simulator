package cloud.cluster.sim.clustersimulator;

import cloud.cluster.sim.clustersimulator.dto.*;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements operations on the Cluster.
 */
@Component
public class ClusterManager {

    private long rpsForOneVm;
    private ClusterExtRep clusterExtRep;
    private Cluster cluster;
    private int currentResourceIndex = 0;
    private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);
    private List<AllocationState> allocationEvolution = new ArrayList<AllocationState>();

    @Autowired
    private CostComputer costComputer;

    /**
     * Read ClusterExtRep configuration date from Json file.
     */
    public ClusterManager() {
        this.rpsForOneVm = SimSettingsExtractor.getSimulationSettings().getRpsForVm();

        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("clusterConfig.json").getFile();

        ObjectMapper mapper = new ObjectMapper();

        try {
            clusterExtRep = mapper.readValue(new File(path), ClusterExtRep.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        clusterExtRepToCluster(clusterExtRep);
    }

    /**
     * Convert external representation of cluster into internal representation.
     *
     * @param clusterExtRep representation of cluster in the clusterConfiguration file.
     */
    private void clusterExtRepToCluster(ClusterExtRep clusterExtRep) {
        List<Vm> vmList = new ArrayList<Vm>();
        clusterExtRep.getTgGroup().stream().forEach(tcGroup -> {
            for (int i = 0; i < tcGroup.getVmNumber(); i++) {
                vmList.add(new Vm(tcGroup.getTreatmentCategory()));
            }
        });
        cluster =  new Cluster(vmList);
    }

//    private void clusterToClusterExtRep() {
//        ClusterExtRep clusterExtRep = new ClusterExtRep(new ArrayList<>());
//
//        for (Vm vm : cluster.getVms()) {
//            TreatmentCategory tc = vm.getTreatmentCategory();
//            if (clusterExtRep.getTgGroup().contains(tc)) {
//
//            }
//            else {
//                clusterExtRep.getTgGroup().add(new TCGroup(tc.getName(), tc.getSla(), 1));
//            }
//        }
//    }

    /**
     * Return the Cluster used by the ClusterManager.
     *
     * @return Cluster used by the ClusterManager.
     */
    Cluster getCluster() {
        return cluster;
    }

    public long getRpsForOneVm() {
        return rpsForOneVm;
    }

    /**
     * Compute the maximum RPS supported by the Cluster managed by the ClusterManager.
     *
     * @return maximum number of request that can be fulfilled by the Cluster in one second.
     */
    public long computeCumulativeRpsForCluster() {
        return cluster.getVms().size() * rpsForOneVm;
    }


    /**
     * Get the number of VMs in the cluster.
     *
     * @return Vm number in the cluster.
     */
    public int getClusterSize() {
        return cluster.getVms().size();
    }

    /**
     * Allows iteration through cluster elements in a circular list fashion while allowing removal of cluster VMs.
     *
     * @return
     */
    public Vm nextVm() {
        if (currentResourceIndex >= getClusterSize()) {
            currentResourceIndex = 0;
        }
        if (getClusterSize() <= 0) {
            return null;
        }
        else {
            return cluster.getVms().get(currentResourceIndex++);
        }
    }

    /**
     * Remove VMs from cluster.
     *
     * @param id of the VM that should be removed.
     */
    public void removeVm(int id) {
        if (cluster.getVms().size() <= 1) {
            return;
        }

        if (id < currentResourceIndex) {
            currentResourceIndex--;
        }
        costComputer.addCostForShutDown(cluster.getVms().get(id));
        cluster.getVms().remove(id);

        compressAllocationEvolution(Reason.SUBTRACT);
    }

    public void failVm(int id) {
        if (id < currentResourceIndex) {
            currentResourceIndex--;
        }
        cluster.getVms().remove(id);

        compressAllocationEvolution(Reason.FAIL);
    }

    /**
     * Add Vms to the cluster, always before the index , in order to allocate tasks to these Vms first.
     *
     * @param vm new VM that needs to be added to the cluster.
     */
    public void addVm(Vm vm) {
        if (currentResourceIndex >= cluster.getVms().size()) {
            cluster.getVms().add(vm);
        }
        else {
            cluster.getVms().add(currentResourceIndex + 1, vm);
        }

        compressAllocationEvolution(Reason.ADD);
    }

    /**
     * Compress traces for allocations and deallocations that happen at the same time.
     *
     * @param reason
     */
    private void compressAllocationEvolution(Reason reason) {
        if (allocationEvolution.size() > 0) {
            AllocationState latAllocationState = allocationEvolution.get(allocationEvolution.size() - 1);

            if(Math.abs(latAllocationState.getTime() - Time.simulationTime) < 0.00000001) {
                latAllocationState.setVmNumber(
                        (reason == Reason.ADD) ?
                                latAllocationState.getVmNumber() + 1 :
                                latAllocationState.getVmNumber() - 1);
                return;
            }
        }

        allocationEvolution.add(new AllocationState(Time.simulationTime,
                (reason == Reason.ADD) ? +1: -1,
                reason));
    }

    /**
     * Traces for VM allocation evolution.
     *
     * @return a list with evolution traces.
     */
    public List<AllocationState> getAllocationEvolution() {
        return allocationEvolution;
    }


    public CostComputer getCostComputer() {
        return costComputer;
    }
}
