package cloudsimulator.requestsimulator.dao;

import cloudsimulator.requestsimulator.dto.SimulationStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SimulationStatisticsOperations extends MongoRepository<SimulationStatistics, String> {
}
