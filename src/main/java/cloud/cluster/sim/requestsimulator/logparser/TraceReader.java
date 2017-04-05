package cloud.cluster.sim.requestsimulator.logparser;

import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

public class TraceReader {

    private static final Logger logger = LoggerFactory.getLogger(TraceReader.class);

    private BufferedReader br = null;
    private String[] files = null;
    private int fileNumber = 0;

    public TraceReader(String pathToTraces, String traceNameRegex) throws IOException {
        files = Files.walk(Paths.get(pathToTraces))
                 .filter(Files::isRegularFile)                                                                           // only consider files
                 .filter(filePath -> filePath.toString().contains(traceNameRegex))                                       // only files that contain traces in name
                 .sorted(Comparator.naturalOrder())
                 .map(file -> file.toString())
                 .toArray(String[]::new);
    }

    /**
     *
     *
     * @return null if there are no more file to read, or a file if there are files.
     * @throws FileNotFoundException
     */
    @Nullable
    private BufferedReader readFromNextFile() throws FileNotFoundException {
        if (fileNumber < files.length) {
            br = new BufferedReader(new FileReader(files[fileNumber]));
            fileNumber++;
            return br;
        }
        else {
            return null;
        }
    }

    @Nullable
    public String getNextTrace() throws IOException {
        String traceLine = null;

        if (br == null) {
            // if there are no more files to read.
            if ((br = readFromNextFile()) == null) {
                return null;
            }
        }

        boolean checkNextFile = false;

        // search for the next line in the current file or in the next ones
        while (traceLine == null) {
            if (checkNextFile) {
                // if there are no more files to read.
                if ((br = readFromNextFile()) == null) {
                    return null;
                }
            }

            traceLine = br.readLine();
            checkNextFile = true;
        }

        return traceLine;
    }

    public static void main(String[] args) {
        try {
            TraceReader tr = new TraceReader("traces", "trimmed");
            String line;
            int counter = 0;

            while ((line = tr.getNextTrace()) != null) {
                counter++;
            }

            System.out.println("Counted " + counter +" lines");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

