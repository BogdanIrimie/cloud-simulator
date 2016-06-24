import cloudsimulator.clustersimulator.ConfigurationReader;
import org.junit.Test;

public class TestConfigurationReader {

    @Test
    public void testReadClusterConfiguration() {
        ConfigurationReader configurationReader = new ConfigurationReader();
        configurationReader.readClusterConfiguration();
    }
}
