package com.middleware.apitv.repository;

import com.middleware.apitv.dto.AnalisisShow;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalisisShowRepository extends MongoRepository<AnalisisShow, String> {
	List<AnalisisShow> findByShowId(Long showId);
}
