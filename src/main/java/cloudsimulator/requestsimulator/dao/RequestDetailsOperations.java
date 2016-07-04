package cloudsimulator.requestsimulator.dao;

import cloudsimulator.requestsimulator.dto.RequestDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Define operations on request documents.
 */
public interface RequestDetailsOperations extends MongoRepository<RequestDetails, String> {

}
