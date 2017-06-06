package ar.com.notarip.teocom.graphs.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.com.notarip.teocom.graphs.domain.Edge;

public interface EdgeRepository extends CrudRepository<Edge, Long>{
//	
//	List<Edge> findBySource(Long source);
//	List<Edge> findByData(Long data);
	
	List<Edge> findAll();
	List<Edge> findByDataSetIdInAndYear(List<Long> dataSetId, Long year);
	Edge findBySourceAndTargetAndDataSetIdAndYear(Long source, Long target, Long dataSetId, Long year);

	@Modifying
	@Transactional
	@Query("delete from edge e where e.dataSetId in (:dataSetIds) and e.year = :year")
	void deleteByDataSetIdInAndYear(@Param("dataSetIds")List<Long> dataSetId, @Param("year")Long year);


}
