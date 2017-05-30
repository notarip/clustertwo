package ar.com.notarip.teocom.graphs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.com.notarip.teocom.graphs.domain.Score;

public interface ScoreRepository extends CrudRepository<Score, Long>{
	
	List<Score> findByDatasetIdAndYear(Long datasetId, int year);
	
	@Query("select s from scores s where s.score between :from and :to and s.datasetId = :dataset and s.year = :year")
	List<Score> findByScoreBetweenFromToAndDatasourceIdAndYear(@Param("from") double from, @Param("to") double to, @Param("dataset") Long dataset, @Param("year") int year);

}
