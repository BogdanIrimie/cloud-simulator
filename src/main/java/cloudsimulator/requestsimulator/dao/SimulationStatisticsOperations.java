package cloudsimulator.requestsimulator.dao;

import cloudsimulator.requestsimulator.dto.SimulationStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Define operations on SimulationStatistics
 */
public interface SimulationStatisticsOperations extends MongoRepository<SimulationStatistics, String> {
}
