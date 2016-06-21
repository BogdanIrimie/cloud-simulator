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

        System.out.println("Operations time outed " + requestsTimeout);
        System.out.println("Average response time is: " + totalDelay / requestCounter);
        httpRequestOpperations.insert(requestList);
    }

    private  double time = -1;
    private long requestsTimeout = 0;
    private long requestCounter;
    private double totalDelay = 0;
    private double delay = 0;

    private double timePerRequest = 1.0 / 1000;
    private List<RequestDetails> requestList = new ArrayList<>();


    public void count(double requestTime) {
        if (time < 0) {
            time = requestTime;
        }

        if (requestTime >= time) {
            time = requestTime;
        }

        if (requestTime + 5 >= time) {
            time += timePerRequest;

            requestCounter++;
            delay = time - requestTime;
            totalDelay += delay;
            requestList.add(new RequestDetails(0, delay));
        }
        else {
            requestsTimeout++;
        }

        if (requestCounter % 500000 == 1) {
            httpRequestOpperations.insert(requestList);
            requestList = new ArrayList<>();
        }

    }
}
