package cloudsimulator.requestsimulator.dao;

import cloudsimulator.requestsimulator.dto.RequestDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HttpRequestOperations extends MongoRepository<RequestDetails, String> {

}
