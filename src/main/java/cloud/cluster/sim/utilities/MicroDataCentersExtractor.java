package cloud.cluster.sim.utilities;


import cloud.cluster.sim.clustersimulator.dto.MicroDataCenter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Extract list of micro data centers.
 */
public class MicroDataCentersExtractor {
    private final Logger logger = LoggerFactory.getLogger(MicroDataCentersExtractor.class);

    /**
     * Extract list of micro data centers from json file.
     *
     * @return List with all possible micro data centers.
     */
    public List<MicroDataCenter> extractMicroDataCenters() {
        List<MicroDataCenter> mDClist = null;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("microDataCenters.json");

        ObjectMapper mapper = new ObjectMapper();

        try {
            mDClist = mapper.readValue(is, new TypeReference<List<MicroDataCenter>>(){});
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return mDClist;
    }
}
