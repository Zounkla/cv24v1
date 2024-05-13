package fr.univrouen.cv24.repository;

import fr.univrouen.cv24.model.CV24;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CV24Repository extends MongoRepository<CV24, String> {

    CV24 findByCvId(int id);
}
