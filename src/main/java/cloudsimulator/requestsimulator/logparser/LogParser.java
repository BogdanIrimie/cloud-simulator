package cloudsimulator.requestsimulator.logparser;

import cloudsimulator.clustersimulator.ClusterManager;
import cloudsimulator.clustersimulator.SimulateFailures;
import cloudsimulator.clustersimulator.dto.TCGroup;
import cloudsimulator.controllersimulator.AutoClusterScale;
import cloudsimulator.requestsimulator.dao.HttpRequestOperations;
import cloudsimulator.requestsimulator.dto.RequestDetails;
import cloudsimulator.requestsimulator.dto.SimulationStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class LogParser {

    @Autowired
    private HttpRequestOperations httpRequestOperations;

    @Autowired
    private ClusterManager clusterManager;

    @Autowired
    private AutoClusterScale scale;

    @Autowired
    private SimulateFailures simulateFailures;

    private double time = -1, notificationTime = 0, totalDelay = 0, responseTime = 0, timePerRequest = 1.0 / 3000;
    private long totalRequestCounter, fulfilledRequestCounter, timeOutedRequestCounter, requestInTheLastSecond;
    private long lastKnownRequestNumber;
    private List<RequestDetails> requestList = new ArrayList<>();

    /**
     * Parse traces from log files and feeds them to the simulation.
     *
     * @return simulation results.
     * @throws IOException
     */
    public SimulationStatistics parseLogs() throws IOException {
        httpRequestOperations.deleteAll();

        long startTime = System.nanoTime();

        // Instantiate ClusterManager
        timePerRequest = 1.0 / clusterManager.computeMaxRps();

        Files.walk(Paths.get("traces"))
                .filter(Files::isRegularFile)                                                                           // only consider files
                .filter(filePath -> filePath.toString().contains("trimed"))                                             // only files that contain traces in name
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

        long endTime = System.nanoTime();
        System.out.println("Time spend executing " + (endTime - startTime));
        //httpRequestOperations.insert(requestList);                                                                    // insert last records in database

        long vmNumberAtEnd = clusterManager.getCluster().getTgGroup().stream().mapToLong(TCGroup::getVmNumber).sum();
        System.out.println("VM number at end of simulation: " + vmNumberAtEnd);
        return new SimulationStatistics(
                totalDelay, totalRequestCounter, fulfilledRequestCounter, timeOutedRequestCounter);
    }

    /**
     * Create RequestDetails object from trace.
     *
     * @param splitTraceLine Array of Strings from one trace.
     */
    private void parseLog(String[] splitTraceLine) {
        long requestId = Long.parseLong(splitTraceLine[0]);
        double requestTime = Double.parseDouble(splitTraceLine[1]);
        RequestDetails requestDetails = new RequestDetails(requestId, requestTime);
        simulateTimePassing(requestDetails);
    }

    /**
     * Simulate time passing in order to compute response time for requests in trace.
     *
     * @param requestDetails contains all the details necessary to compute response time for a trace.
     */
    private void simulateTimePassing(RequestDetails requestDetails) {
        double requestTime = requestDetails.getRequestArrivalTime();
        if (time < 0) {
            time = requestTime;
            notificationTime = time + 1;
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
            //httpRequestOperations.insert(requestList);
            requestList = new ArrayList<>();
        }

        // Notify other components of time passing, in 1 second increments.
        if (requestTime >= notificationTime) {
            requestInTheLastSecond = totalRequestCounter - lastKnownRequestNumber;
            lastKnownRequestNumber = totalRequestCounter;

            // each second notify the auto scaling
            scale.scalePolicy(clusterManager, requestInTheLastSecond);

            //simulate failure
            // simulateFailures.simulateFailures(clusterManager.getCluster());

            // Recompute time per request because the cluster configuration might have changed.
            timePerRequest = 1.0 / clusterManager.computeMaxRps();

            // Set next notification time with 1 second increment.
            notificationTime = requestTime + 1;
            // TODO
            /*
            Time can be incremented with more then one second in current implementation.
            Maybe add a small mechanism so simulate time passing in increments of 1 second
            to send notifications to other components even when no requests are received.
            */
        }

    }
}
