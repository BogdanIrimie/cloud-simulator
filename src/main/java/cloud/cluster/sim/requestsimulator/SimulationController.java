package cloud.cluster.sim.requestsimulator;

import cloud.cluster.sim.clustersimulator.ClusterManager;
import cloud.cluster.sim.clustersimulator.FailureInjector;
import cloud.cluster.sim.clustersimulator.dto.Task;
import cloud.cluster.sim.controllersimulator.AutoClusterScale;
import cloud.cluster.sim.requestsimulator.dao.SimulationStatisticsOperations;
import cloud.cluster.sim.requestsimulator.dto.RequestDetails;
import cloud.cluster.sim.requestsimulator.dto.SimulationStatistics;
import cloud.cluster.sim.requestsimulator.logparser.TraceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimulationController {

    private static final Logger logger = LoggerFactory.getLogger(SimulationController.class);

    @Autowired
    private ClusterManager clusterManager;

    @Autowired
    private AutoClusterScale scale;

    @Autowired
    private FailureInjector failureInjector;

    @Autowired
    private SimulationStatisticsOperations simulationStatisticsOperations;

    private double timePerRequest = 1.0 / 3000, totalDelay, responseTime;
    private long fulfilledRequestCounter,timeOutedRequestCounter, lastKnownRequestNumber, totalRequestCounter;
    private int taskTimeout = 5;

    /**
     * Create RequestDetails object from trace.
     *
     * @param splitTraceLine Array of Strings from one trace.
     */
    private RequestDetails parseLog(String[] splitTraceLine) {
        long requestId = Long.parseLong(splitTraceLine[0]);
        double requestTime = Double.parseDouble(splitTraceLine[1]);
        RequestDetails requestDetails = new RequestDetails(requestId, requestTime);
        return requestDetails;
    }


    public void simulate() {
        String traceLine = null;
        RequestDetails rd = null;

        TraceReader tr = new TraceReader();
        long startTime = System.nanoTime();

        double time = 0;
        double nextTime = 0;
        double requestTime = 0;
        int systemTicCounter = 0;

        timePerRequest =  1.0 / clusterManager.getRpsForOneVm();


        // set the start of the simulation to the time of the first trace
        traceLine = tr.getNextTrace();
        rd = parseLog(traceLine.split(" "));
        time = rd.getRequestArrivalTime();
        nextTime = time + 1;

        double lastTraceTime = 0;

        while (true) {
            requestTime = rd.getRequestArrivalTime();

            // set the next action step for the system
            if (nextTime <= time) {
                nextTime++;
                systemTicCounter++;

                notifyComponentsOfTimePassing();
            }

            // do we have a request that was not fulfilled until the present simulation moment?
            if (time >= requestTime) {
                // is the request within the timeout period?
                if (requestTime + taskTimeout < time) {
                    timeOutedRequestCounter++;
                }
                else {
                    // is there a VM available to fulfill the request
                    Task task = clusterManager.nextVm().getTask();
                    if (time >= task.getTaskEndTime()) {
                        //assign task to VM
                        task.setTaskArrivalTime(requestTime);
                        task.setTaskStartTime(time);
                        task.setTaskEndTime(time + timePerRequest);

                        responseTime = task.getTaskEndTime() - task.getTaskArrivalTime();
                        totalDelay += responseTime;
                        fulfilledRequestCounter++;
                    } else {
                        // there is no VM free;
                        time = Math.min(nextTime, task.getTaskEndTime());
                        continue;
                    }
                }

                // read next trace
                traceLine = tr.getNextTrace();
                if (traceLine == null) {
                    break;
                }
                rd = parseLog(traceLine.split(" "));
                requestTime = rd.getRequestArrivalTime();
                lastTraceTime = requestTime;
            }
            else {
                // increment simulation time because we have no request, maximum increment
                // is still the minimum between simulation unit of time and the request time.
                if (requestTime > time) {
                    time = Math.min(nextTime, requestTime);
                    continue;
                }

                traceLine = tr.getNextTrace();
                if (traceLine == null) {
                    break;
                }
                rd = parseLog(traceLine.split(" "));
                requestTime = rd.getRequestArrivalTime();
                lastTraceTime = requestTime;

                time =  Math.min(nextTime, requestTime);
            }
        }

        totalRequestCounter = fulfilledRequestCounter + timeOutedRequestCounter;

        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1000000000;
        logger.info("Time spend executing:           " + executionTime + " seconds");

        logger.info("End time of simulation was: " + String.format("%.10f", time));
        logger.info("Last known time was:        " + String.format("%.10f", lastTraceTime));
        logger.info("Request processed:          " + totalRequestCounter);
        logger.info("There were:                 " + systemTicCounter + " tics");

        long vmNumberAtEnd = clusterManager.getClusterSize();
        logger.info("VM number at end of simulation: " + vmNumberAtEnd);
        SimulationStatistics simulationStatistics = new SimulationStatistics(
                totalDelay, totalRequestCounter, fulfilledRequestCounter, timeOutedRequestCounter,
                clusterManager.getCostComputer().getTotalCost(), executionTime, clusterManager.getAllocationEvolution());

        double avgResponseTime = simulationStatistics.getTotalDelay()
                / simulationStatistics.getFulfilledRequestCounter();

        simulationStatisticsOperations.insert(simulationStatistics);
        logger.info("Average response time was:      " + String.format("%.10f", avgResponseTime));
        logger.info("Number of requests dropped:     " + simulationStatistics.getTimeOutedRequestCounter());
        logger.info("Number of request fulfilled:    " + simulationStatistics.getFulfilledRequestCounter());
        logger.info("Total cost:                     " + simulationStatistics.getTotalCost());
    }

    private void notifyComponentsOfTimePassing() {
        long requestInLastSecond = fulfilledRequestCounter + timeOutedRequestCounter - lastKnownRequestNumber;
        lastKnownRequestNumber = fulfilledRequestCounter + timeOutedRequestCounter;

        // each seimulation unit of time notify the auto scaling
        scale.scalePolicy(clusterManager, requestInLastSecond);
        clusterManager.getCostComputer().addCostForLastSecond(clusterManager);

        // compute cost
        clusterManager.getCostComputer().addCostForLastSecond(clusterManager);
        scale.incrementTime();

        //simulate failure
        failureInjector.injectFailure(clusterManager);
    }
}
