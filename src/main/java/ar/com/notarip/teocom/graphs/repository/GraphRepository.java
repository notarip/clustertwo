package ar.com.notarip.teocom.graphs.repository;

import org.springframework.data.repository.CrudRepository;

import ar.com.notarip.teocom.graphs.domain.Graph;

public interface GraphRepository extends CrudRepository<Graph, Long>{
	
	
}
