package cloud.cluster.sim.requestsimulator.simulators;

import cloud.cluster.sim.clustersimulator.ClusterManager;
import cloud.cluster.sim.clustersimulator.FailureInjector;
import cloud.cluster.sim.clustersimulator.dto.ClusterExtRep;
import cloud.cluster.sim.clustersimulator.dto.Task;
import cloud.cluster.sim.clustersimulator.dto.Time;
import cloud.cluster.sim.controllersimulator.AutoClusterScale;
import cloud.cluster.sim.requestsimulator.dao.SimulationStatisticsOperations;
import cloud.cluster.sim.requestsimulator.dto.RequestDetails;
import cloud.cluster.sim.requestsimulator.dto.SimulationStatistics;
import cloud.cluster.sim.requestsimulator.logparser.TraceReader;
import cloud.cluster.sim.utilities.SimSettingsExtractor;
import cloud.cluster.sim.utilities.dto.SimulationSettings;
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

    private double timePerRequest, responseTime;
    private long fulfilledRequestCounter,timeOutedRequestCounter, lastKnownRequestNumber;
    private int taskTimeout;

    public void startSimulation() {
        long totalRequestCounter = 0, totalDelay = 0;
        double time = 0, nextTime = 0, requestTime = 0, minLatency, maxLatency;
        int simulationTics = 0;
        ClusterExtRep initialClusterState, finalClusterState;

        RequestDetails traceLine = null;
        TraceReader tr = new TraceReader();

        // initialize simulation settings
        timePerRequest = 1.0 / clusterManager.getRpsForOneVm();
        taskTimeout = SimSettingsExtractor.getSimulationSettings().getTaskTimeout();
        minLatency = taskTimeout;
        maxLatency = 0;


        long startTime = System.nanoTime();

        initialClusterState = clusterManager.getClusterExtRep();

        // set the start of the simulation to the time of the first trace
        traceLine = tr.getNextTrace();
        time = traceLine.getRequestArrivalTime();

        nextTime = time + 1;

        while (true) {
            requestTime = traceLine.getRequestArrivalTime();

            // set the next action step for the system
            if (nextTime <= time) {
                nextTime++;
                simulationTics++;

                Time.simulationTime = time;
                notifyComponentsOfTimePassing();
            }

            // do we have a request that was not fulfilled until the present simulation moment?
            if (time >= requestTime) {
                // is the request within the timeout period?
                if (requestTime + taskTimeout < time) {
                    timeOutedRequestCounter++;
                } else {
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

                        // compute max and min response time for simulation
                        if (responseTime < minLatency) {
                            minLatency = responseTime;
                        }
                        if (responseTime > maxLatency) {
                            maxLatency = responseTime;
                        }

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
                requestTime = traceLine.getRequestArrivalTime();
            } else {
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
                requestTime = traceLine.getRequestArrivalTime();

                time = Math.min(nextTime, requestTime);
            }
        }

        finalClusterState = clusterManager.getClusterExtRep();
        totalRequestCounter = fulfilledRequestCounter + timeOutedRequestCounter;

        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1000000000;

        SimulationStatistics simulationStatistics = new SimulationStatistics(
                totalDelay, minLatency, maxLatency, totalRequestCounter, fulfilledRequestCounter,
                timeOutedRequestCounter, simulationTics, clusterManager.getCostComputer().getTotalCost(),
                initialClusterState, finalClusterState, executionTime, clusterManager.getAllocationEvolution());

        saveSimulationResults(simulationStatistics);
    }

    /**
     * Save simulation statistics in DB and print them.
     *
     * @param simulationStatistics contains all the statistics that should be saved related to one simulation.
     */
    private void saveSimulationResults(SimulationStatistics simulationStatistics) {
        long vmNumberAtEndOfSimulation = clusterManager.getClusterSize();

        logger.info("Time spend executing:           " + simulationStatistics.getExecutionTime() + " seconds");
        logger.info("Request processed:              " + simulationStatistics.getTotalRequestCounter());
        logger.info("VM number at end of simulation: " + vmNumberAtEndOfSimulation);
        logger.info("Average response time was:      " + String.format("%.10f", simulationStatistics.getAvgResponseTime()));
        logger.info("Number of requests dropped:     " + simulationStatistics.getTimeOutedRequestCounter());
        logger.info("Number of request fulfilled:    " + simulationStatistics.getFulfilledRequestCounter());
        logger.info("Total cost:                     " + simulationStatistics.getTotalCost());
        logger.info("Simulation tics:                " + simulationStatistics.getSimulationTics());

        simulationStatisticsOperations.insert(simulationStatistics);
    }

    /**
     * Notify other simulator components that a unit of simulation time has passed.
     */
    private void notifyComponentsOfTimePassing() {
        long requestInLastSecond = fulfilledRequestCounter + timeOutedRequestCounter - lastKnownRequestNumber;
        lastKnownRequestNumber = fulfilledRequestCounter + timeOutedRequestCounter;

        // compute cost
        clusterManager.getCostComputer().addCostForLastSecond(clusterManager);

        // each simulation unit of time notify the auto scaling
        scale.scalePolicy(clusterManager, requestInLastSecond);

        //simulate failure
        failureInjector.injectFailure(clusterManager);
    }
}
