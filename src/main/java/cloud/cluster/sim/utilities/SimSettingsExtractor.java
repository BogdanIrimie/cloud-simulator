package cloud.cluster.sim.utilities;

import cloud.cluster.sim.utilities.dto.SimulationSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

public class SimSettingsExtractor {

    private static final Logger logger = LoggerFactory.getLogger(SimSettingsExtractor.class);
    private static SimulationSettings simulationStatistics;

    public static SimulationSettings getSimulationSettings() {
        if (simulationStatistics == null) {
            simulationStatistics = extractSimulationSettings();
        }
        return simulationStatistics;
    }

    private static SimulationSettings extractSimulationSettings() {
        InputStream is = SimSettingsExtractor.class.getClassLoader().getResourceAsStream("simulationSettings.json");
        ObjectMapper mapper = new ObjectMapper();

        try {
            simulationStatistics = mapper.readValue(is, SimulationSettings.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return simulationStatistics;
    }

}
