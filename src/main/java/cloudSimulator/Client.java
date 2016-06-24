package cloudsimulator;

import cloudsimulator.requestsimulator.simulators.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Client implements CommandLineRunner {

    @Autowired
    Simulator simulator;

    public static void main (String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        simulator.startSimulation();
    }

}
