package cloud.cluster.sim.requestsimulator.logparser;

import cloud.cluster.sim.utilities.SimSettingsExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

/**
 * Read trace data from files.
 */
public class TraceReader {

    private static final Logger logger = LoggerFactory.getLogger(TraceReader.class);

    private BufferedReader br = null;
    private String[] fileNames = null;
    private int fileNumber = 0;

    /**
     * Stores all file names from which we should read trace data.
     *
     * @throws IOException
     */
    public TraceReader() {
        String pathToTraces = SimSettingsExtractor.getSimulationSettings().getPathToTraces();
        String traceNameRegex = SimSettingsExtractor.getSimulationSettings().getRegexForTracesName();

        try {
            fileNames = Files.walk(Paths.get(pathToTraces))
                     .filter(Files::isRegularFile)                                                                           // only consider fileNames
                     .filter(filePath -> filePath.toString().contains(traceNameRegex))                                       // only fileNames that contain traces in name
                     .sorted(Comparator.naturalOrder())
                     .map(file -> file.toString())
                     .toArray(String[]::new);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Provide the next line of trace data.
     *
     * @return a line of trace data or null if there are no more lines to read.
     * @throws IOException
     */
    public String getNextTrace() {
        String traceLine = null;

        try {
            if (br == null) {
                // if there are no more fileNames to read.
                if ((br = readFromNextFile()) == null) {
                    return null;
                }
            }

            boolean checkNextFile = false;

            // search for the next line in the current file or in the next ones
            while (traceLine == null) {
                if (checkNextFile) {
                    // if there are no more fileNames to read.
                    if ((br = readFromNextFile()) == null) {
                        return null;
                    }
                }

                traceLine = br.readLine();
                checkNextFile = true;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return  traceLine;
    }

    /**
     * Provides a buffered reader for the next file.
     *
     * @return null if there are no more file to read, or a file if there are files.
     * @throws FileNotFoundException
     */
    private BufferedReader readFromNextFile() throws FileNotFoundException {
        if (fileNumber < fileNames.length) {
            br = new BufferedReader(new FileReader(fileNames[fileNumber]));
            fileNumber++;
            return br;
        }
        else {
            return null;
        }
    }

}

