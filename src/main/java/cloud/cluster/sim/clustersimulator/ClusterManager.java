package cloud.cluster.sim.clustersimulator;

import cloud.cluster.sim.clustersimulator.dto.*;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);

    /**
     * Read ClusterExtRep configuration date from Json file.
     */
    public ClusterManager() {
        this.rpsForOneVm = SimSettingsExtractor.getSimulationSettings().getRpsForVm();

        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("clusterConfiguration.json").getFile();

        ObjectMapper mapper = new ObjectMapper();

        try {
            clusterExtRep = mapper.readValue(new File(path), ClusterExtRep.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        // ClusterExtRep to Cluster
        List<Vm> vmList = new ArrayList<Vm>();
        clusterExtRep.getTgGroup().stream().forEach(tcGroup -> {
            TreatmentCategory tc =  new TreatmentCategory(tcGroup.getName(), tcGroup.getSla(), tcGroup.getCost());
            for (int i = 0; i < tcGroup.getVmNumber(); i++) {
                vmList.add(new Vm(tc));
            }
        });
        cluster = new Cluster(vmList);
    }

    /**
     * Return the Cluster used by the ClusterManager.
     *
     * @return Cluster used by the ClusterManager.
     */
    public Cluster getCluster() {
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
    public long computeMaxRps() {
        return cluster.getVms().size() * rpsForOneVm;
    }

}
