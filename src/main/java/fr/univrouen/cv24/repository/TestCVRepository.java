package fr.univrouen.cv24.repository;

import fr.univrouen.cv24.model.TestCV;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestCVRepository extends MongoRepository<TestCV, Integer> {

    TestCV findByDate(String name);

    TestCV findTopByOrderByIdDesc();
}
