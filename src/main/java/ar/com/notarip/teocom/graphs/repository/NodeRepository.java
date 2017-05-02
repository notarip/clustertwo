package ar.com.notarip.teocom.graphs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ar.com.notarip.teocom.graphs.domain.Node;

public interface NodeRepository extends CrudRepository<Node, Long>{
	
	Node findById(Long source);

}
