package cloudsimulator.requestsimulator.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration{
    @Override
    protected String getDatabaseName() {
        return "Requests";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("192.168.56.101");
    }
}
