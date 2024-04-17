package fr.univrouen.cv24.repository;

import fr.univrouen.cv24.model.CV24;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CV24Repository extends MongoRepository<CV24, Integer> {

    CV24 findTopByOrderByIdDesc();
}
