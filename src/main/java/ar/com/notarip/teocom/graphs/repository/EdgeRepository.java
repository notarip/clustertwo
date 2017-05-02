package ar.com.notarip.teocom.graphs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ar.com.notarip.teocom.graphs.domain.Edge;

public interface EdgeRepository extends CrudRepository<Edge, Long>{
	
	List<Edge> findBySource(Long source);
	List<Edge> findByData(Long data);

}
