package cloudSimulator;

import cloudSimulator.dao.HttpRequestOpperations;
import cloudSimulator.dto.RequestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class Client implements CommandLineRunner {


    @Autowired
    private HttpRequestOpperations httpRequestOpperations;


    public static void main (String[] args) {
        SpringApplication.run(Client.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
        httpRequestOpperations.deleteAll();
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

        System.out.println("Operations time outed " + countNumberOfRequestsTimeOuted);
        System.out.printf("Average response time is: %f\n", (totalDelay / fulfilledRequestCounter));
        httpRequestOpperations.insert(requestList);
    }

    public void parseLog(String[] splittedTraceLine) {
        long requestId = Long.parseLong(splittedTraceLine[0]);
        double requestTime = Double.parseDouble(splittedTraceLine[1]);
        RequestDetails requestDetails = new RequestDetails(requestId, requestTime);
        simulateTimePassing(requestDetails);
    }

    private  double time = -1;
    private long countNumberOfRequestsTimeOuted = 0;
    private long totalRequestCounter = 0, fulfilledRequestCounter = 0, timeOutedRequestCounter = 0;
    private double totalDelay = 0;
    private double responseTime = 0;

    private double timePerRequest = 1.0 / 9000;
    private List<RequestDetails> requestList = new ArrayList<RequestDetails>();


    public void simulateTimePassing(RequestDetails requestDetails) {
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
            countNumberOfRequestsTimeOuted++;
            timeOutedRequestCounter++;
            responseTime = -1;
        }

        requestDetails.setResponseTime(responseTime);
        requestList.add(requestDetails);

        totalRequestCounter = fulfilledRequestCounter + timeOutedRequestCounter;
        if (totalRequestCounter % 500000 == 1) {
            httpRequestOpperations.insert(requestList);
            requestList = new ArrayList<RequestDetails>();
        }

    }

}
