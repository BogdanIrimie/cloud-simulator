package cloudSimulator.dao;

import cloudSimulator.dto.RequestDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HttpRequestOpperations extends MongoRepository<RequestDetails, String> {

}
