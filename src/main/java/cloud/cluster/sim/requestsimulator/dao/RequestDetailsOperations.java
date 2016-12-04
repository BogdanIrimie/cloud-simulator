package cloud.cluster.sim.requestsimulator.dao;

import cloud.cluster.sim.requestsimulator.dto.RequestDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * Define operations on request documents.
 */
@Component
public interface RequestDetailsOperations extends MongoRepository<RequestDetails, String> {

}
