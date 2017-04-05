import cloud.cluster.sim.clustersimulator.dto.MicroDataCenter;
import cloud.cluster.sim.utilities.MicroDataCentersExtractor;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestMicroDaraCentersExtractor {

    @Test
    public void testParsMicroDataCentersJson() {
        MicroDataCentersExtractor microDataCentersExtractor = new MicroDataCentersExtractor();
        List<MicroDataCenter> mDC = microDataCentersExtractor.extractMicroDataCenters();
        assertTrue(mDC.size() == 3);
    }
}
