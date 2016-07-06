package cloud.cluster.sim.requestsimulator.dao;

import cloud.cluster.sim.requestsimulator.dto.SimulationStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Define operations on SimulationStatistics
 */
public interface SimulationStatisticsOperations extends MongoRepository<SimulationStatistics, String> {
}
