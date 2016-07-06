package cloud.cluster.sim.requestsimulator.dao;

import cloud.cluster.sim.requestsimulator.dto.RequestDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Define operations on request documents.
 */
public interface RequestDetailsOperations extends MongoRepository<RequestDetails, String> {

}
