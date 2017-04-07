package cloud.cluster.sim.requestsimulator;

import cloud.cluster.sim.clustersimulator.ClusterManager;
import cloud.cluster.sim.clustersimulator.dto.Task;
import cloud.cluster.sim.requestsimulator.dto.RequestDetails;
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

    private double timePerRequest = 1.0 / 3000, totalDelay, responseTime;
    private long fulfilledRequestCounter,timeOutedRequestCounter, lastKnownRequestNumber;
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

        int counter = 0;
        long startTime = System.nanoTime();

        double time = 0;
        double nextTime = 0;
        double requestTime = 0;
        int systemTicCounter = 0;

        // set the start of the simulation to the time of the first trace
        traceLine = tr.getNextTrace();
        rd = parseLog(traceLine.split(" "));
        time = rd.getRequestArrivalTime();
        nextTime = time + 1;

        double lastTraceTime = 0;

        while (true) {
            requestTime = rd.getRequestArrivalTime();
            counter++;

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


        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1000000000;
        logger.info("Time spend executing:           " + executionTime + " seconds");

        logger.info("End time of simulation was: " + String.format("%.10f", time));
        logger.info("Last known time was:        " + String.format("%.10f", lastTraceTime));
        logger.info("Counted:                    " + counter +" lines");
        logger.info("Request processed:          " + (fulfilledRequestCounter + timeOutedRequestCounter));
        logger.info("There were:                 " + systemTicCounter + " tics");
    }

    private void notifyComponentsOfTimePassing() {
        long requestInLastSecond = fulfilledRequestCounter + timeOutedRequestCounter - lastKnownRequestNumber;
        lastKnownRequestNumber = fulfilledRequestCounter + timeOutedRequestCounter;
    }


}
