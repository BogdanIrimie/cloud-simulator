package cloudsimulator.utilities;

import cloudsimulator.utilities.dto.SimulationSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

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
        String path = SimSettingsExtractor.class.getClassLoader().getResource("simulationSettings.json").getFile();
        ObjectMapper mapper = new ObjectMapper();

        try {
            simulationStatistics = mapper.readValue(new File(path), SimulationSettings.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return simulationStatistics;
    }

}
