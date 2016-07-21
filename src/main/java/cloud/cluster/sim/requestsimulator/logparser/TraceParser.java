package cloud.cluster.sim.requestsimulator.logparser;

import cloud.cluster.sim.clustersimulator.dto.*;
import cloud.cluster.sim.requestsimulator.dto.RequestDetails;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import cloud.cluster.sim.clustersimulator.ClusterManager;
import cloud.cluster.sim.clustersimulator.FailureInjector;
import cloud.cluster.sim.controllersimulator.AutoClusterScale;
import cloud.cluster.sim.requestsimulator.dao.RequestDetailsOperations;
import cloud.cluster.sim.requestsimulator.dto.SimulationStatistics;
import cloud.cluster.sim.utilities.CostComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TraceParser {

    @Autowired
    private RequestDetailsOperations requestDetailsOperations;

    @Autowired
    private ClusterManager clusterManager;

    @Autowired
    private AutoClusterScale scale;

    @Autowired
    private FailureInjector failureInjector;

    @Autowired
    private CostComputer costComputer;

    private static final Logger logger = LoggerFactory.getLogger("LogParser.class");

    private double time = -1, notificationTime = 0, totalDelay = 0, responseTime = 0, timePerRequest = 1.0 / 3000;
    private long totalRequestCounter, fulfilledRequestCounter, timeOutedRequestCounter, requestInTheLastSecond;
    private long lastKnownRequestNumber;
    private int taskTimeout = 5;
    private List<RequestDetails> requestList = new ArrayList<>();

    /**
     * Parse traces from log files and feeds them to the simulation.
     *
     * @return simulation results.
     * @throws IOException
     */
    public SimulationStatistics parseTraces() throws IOException {
        requestDetailsOperations.deleteAll();

        long startTime = System.nanoTime();

        // Instantiate ClusterManager
        timePerRequest = 1.0 / 1000;//1.0 / clusterManager.computeMaxRps();

        String pathToTraces = SimSettingsExtractor.getSimulationSettings().getPathToTraces();
        String traceNameRegex = SimSettingsExtractor.getSimulationSettings().getRegexForTracesName();

        Files.walk(Paths.get(pathToTraces))
                .filter(Files::isRegularFile)                                                                           // only consider files
                .filter(filePath -> filePath.toString().contains(traceNameRegex))                                             // only files that contain traces in name
                .sorted(Comparator.naturalOrder())                                                                      // sort file by name
                .forEach(filePath -> {
                    logger.info("Trace path:                     " + filePath);
                    try {
                        Files.lines(Paths.get(filePath.toString()), Charset.forName("Cp1252"))                          // Open file and read lines
                                .map(line -> line.split("\\s+"))
                                .forEach(splittedLine -> parseLog(splittedLine));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1000000000;
        logger.info("Time spend executing:           " + executionTime + " seconds");
        // requestDetailsOperations.insert(requestList);                                                                    // insert last records in database

        long vmNumberAtEnd = clusterManager.getClusterNg().getVms().size();
        logger.info("VM number at end of simulation: " + vmNumberAtEnd);
        return new SimulationStatistics(
                totalDelay, totalRequestCounter, fulfilledRequestCounter,
                timeOutedRequestCounter, costComputer.getTotalCost(), executionTime);
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
//        simulateTimePassing(requestDetails);
        broker(requestDetails);
    }

    private List<Vm> vms = new ArrayList<Vm>(){{
        add(new Vm(new Task(), new TreatmentCategory()));
        add(new Vm(new Task(), new TreatmentCategory()));
        add(new Vm(new Task(), new TreatmentCategory()));
    }};

    private int vmIndex = 0;

    private void broker(RequestDetails requestDetails) {
        if (vmIndex >= vms.size()) {
            vmIndex = 0;
        }

        double requestTime = requestDetails.getRequestArrivalTime();
        if (time < 0) {
            time = requestTime;
        }

        if (requestTime >= time) {
            time = requestTime;
        }

        if (requestTime + taskTimeout >= time) {

            Task task = vms.get(vmIndex++).getTask();
            if (time  < task.getTaskEndTime()) {
                time = task.getTaskEndTime();
            }

            task.setTaskArrivalTime(requestTime);
            task.setTaskStartTime(time);
            task.setTaskEndTime(time + timePerRequest);

            responseTime = task.getTaskEndTime() - task.getTaskArrivalTime();
            totalDelay += responseTime;

            fulfilledRequestCounter++;
        }
        else {
            timeOutedRequestCounter++;
            responseTime = -1;
        }

        totalRequestCounter = fulfilledRequestCounter + timeOutedRequestCounter;
    }

    /**
     * Simulate time passing in order to compute response time for requests in trace.
     *
     * @param requestDetails contains all the details necessary to compute response time for a trace.
     */
//    private void simulateTimePassing(RequestDetails requestDetails) {
//        double requestTime = requestDetails.getRequestArrivalTime();
//        if (time < 0) {
//            time = requestTime;
//            notificationTime = time + 1;
//        }
//
//        if (requestTime >= time) {
//            time = requestTime;
//        }
//
//        if (requestTime + taskTimeout >= time) {
//            time += timePerRequest;
//            fulfilledRequestCounter++;
//
//            responseTime = time - requestTime;
//            totalDelay += responseTime;
//        }
//        else {
//            timeOutedRequestCounter++;
//            responseTime = -1;
//        }
//
//        requestDetails.setResponseTime(responseTime);
//        requestList.add(requestDetails);
//
//        totalRequestCounter = fulfilledRequestCounter + timeOutedRequestCounter;
//        if (totalRequestCounter % 500000 == 1) {                                                                        // Insert request in bulks for better performance
//            // requestDetailsOperations.insert(requestList);
//            requestList = new ArrayList<>();
//        }
//
//        // Notify other components of time passing, in 1 second increments.
//        if (requestTime >= notificationTime) {
//            requestInTheLastSecond = totalRequestCounter - lastKnownRequestNumber;
//            lastKnownRequestNumber = totalRequestCounter;
//
//            // compute cost
//            costComputer.addCostForLastSecond(clusterManager);
//
//            // each second notify the auto scaling
//            scale.scalePolicy(clusterManager, requestInTheLastSecond);
//
//            //simulate failure
//            // failureInjector.injectFailure(clusterManager.getCluster());
//
//            // Recompute time per request because the cluster configuration might have changed.
//            timePerRequest = 1.0 / clusterManager.computeMaxRps();
//
//            // Set next notification time with 1 second increment.
//            notificationTime = requestTime + 1;
//            // TODO
//            /*
//            Time can be incremented with more then one second in current implementation.
//            Maybe add a small mechanism so simulate time passing in increments of 1 second
//            to send notifications to other components even when no requests are received.
//            */
//        }
//
//    }
}
