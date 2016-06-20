import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

public class Client {

    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("traces"))
                .filter(Files::isRegularFile)           // only consider files
                .filter(filePath -> filePath.toString().contains("traces_"))    // only files that contain traces in name
                .sorted(Comparator.naturalOrder())      // sort file name
                .forEach(filePath -> {                  // for each file
                    System.out.println(filePath);
                    try {
                        Files.lines(Paths.get(filePath.toString()), Charset.forName("Cp1252"))  // Open file and read lines
                                .map(line -> line.split("\\s+")[1])     // extract the second record from the file
                                .map(line -> Double.parseDouble(line))
                                .forEach(line -> count(line));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        });

        System.out.println("Request time outed " + requestsTimeout);
    }

    private static final long REQUESTS_SECOND = 3000 / 10;
    private static final double TIME_INTERVAL = 0.1;
    private static double time = -1;
    private static long requestCounter = REQUESTS_SECOND;
    private static long requestsTimeout = 0;

    public static void count(double requestTime) {
        if (time < 0) {
            time = (long)requestTime;
        }

        if ((long)requestTime + 5 >= time) {
            requestCounter--;
        }
        else {
            requestsTimeout++;
        }


        if (requestCounter == 0 || requestTime >= time + TIME_INTERVAL) {
            time += TIME_INTERVAL;
            requestCounter = REQUESTS_SECOND;
        }
    }
}
