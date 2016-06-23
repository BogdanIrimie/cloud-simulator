package cloudsimulator.requestsimulator.logparser;

import cloudsimulator.requestsimulator.dao.HttpRequestOperations;
import cloudsimulator.requestsimulator.dto.RequestDetails;
import cloudsimulator.requestsimulator.dto.SimulationStatistics;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Read all traces from multiple files. Simulate passing of time and compute response time for request.
 * All the results are saved in database.
 */
public class LogParser {

    @Autowired
    private HttpRequestOperations httpRequestOperations;

    private double time = -1, totalDelay = 0, responseTime = 0, timePerRequest = 1.0 / 9000;;
    private long totalRequestCounter = 0, fulfilledRequestCounter = 0, timeOutedRequestCounter = 0;
    private List<RequestDetails> requestList = new ArrayList<>();

    public SimulationStatistics parseLogs() throws IOException {
        httpRequestOperations.deleteAll();

        Files.walk(Paths.get("traces"))
                .filter(Files::isRegularFile)                                                                           // only consider files
                .filter(filePath -> filePath.toString().contains("traces_"))                                            // only files that contain traces in name
                .sorted(Comparator.naturalOrder())                                                                      // sort file by name
                .forEach(filePath -> {
                    System.out.println(filePath);
                    try {
                        Files.lines(Paths.get(filePath.toString()), Charset.forName("Cp1252"))                          // Open file and read lines
                                .map(line -> line.split("\\s+"))
                                .forEach(splittedLine -> parseLog(splittedLine));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        httpRequestOperations.insert(requestList);                                                                      // insert last records in database
        return new SimulationStatistics(
                totalDelay, totalRequestCounter, fulfilledRequestCounter, timeOutedRequestCounter);
    }

    private void parseLog(String[] splittedTraceLine) {
        long requestId = Long.parseLong(splittedTraceLine[0]);
        double requestTime = Double.parseDouble(splittedTraceLine[1]);
        RequestDetails requestDetails = new RequestDetails(requestId, requestTime);
        simulateTimePassing(requestDetails);
    }

    private void simulateTimePassing(RequestDetails requestDetails) {
        double requestTime = requestDetails.getRequestArrivalTime();
        if (time < 0) {
            time = requestTime;
        }

        if (requestTime >= time) {
            time = requestTime;
        }

        if (requestTime + 5 >= time) {
            time += timePerRequest;
            fulfilledRequestCounter++;

            responseTime = time - requestTime;
            totalDelay += responseTime;
        }
        else {
            timeOutedRequestCounter++;
            responseTime = -1;
        }

        requestDetails.setResponseTime(responseTime);
        requestList.add(requestDetails);

        totalRequestCounter = fulfilledRequestCounter + timeOutedRequestCounter;
        if (totalRequestCounter % 500000 == 1) {                                                                        // Insert request in bulks for better performance
            httpRequestOperations.insert(requestList);
            requestList = new ArrayList<>();
        }

    }
}
