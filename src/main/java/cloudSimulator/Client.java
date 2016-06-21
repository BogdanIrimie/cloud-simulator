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
import java.util.Comparator;

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

        System.out.println("cloudSimulator.dao.HttpRequestOpperations time outed " + requestsTimeout);
        System.out.println("Average response time is: " + delay / counter);
    }

    private int FACTOR = 10; // 1 = 1 sec; 10 = 1/10 sec
    private long REQUESTS_SECOND = 3000 / FACTOR;
    private double TIME_INTERVAL = 1.0 / FACTOR;
    private  double time = -1;
    private long requestCounter = REQUESTS_SECOND;
    private long requestsTimeout = 0;
    private long counter;
    private double delay = 0;

    public void count(double requestTime) {
        if (time < 0) {
            time = Math.floor(requestTime * FACTOR ) / FACTOR;
            //time = (long)requestTime;
        }

        if (requestTime + 5 >= time) {
            requestCounter--;
            counter++;
            delay += time + TIME_INTERVAL - requestTime;
            //httpRequestOpperations.save(new RequestDetails(0,delay));
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
