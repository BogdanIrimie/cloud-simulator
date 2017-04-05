package cloud.cluster.sim.requestsimulator.dao;

import cloud.cluster.sim.requestsimulator.dto.SimulationStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * Define operations on SimulationStatistics
 */
@Component
public interface SimulationStatisticsOperations extends MongoRepository<SimulationStatistics, String> {
}
