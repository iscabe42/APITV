package com.middleware.apitv.repository;

import com.middleware.apitv.dto.AnalisisShow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalisisShowRepository extends MongoRepository<AnalisisShow, String> {
}
