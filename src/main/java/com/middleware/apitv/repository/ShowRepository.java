package com.middleware.apitv.repository;

import com.middleware.apitv.dto.ShowInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends MongoRepository<ShowInfo, Long> {
   
}

