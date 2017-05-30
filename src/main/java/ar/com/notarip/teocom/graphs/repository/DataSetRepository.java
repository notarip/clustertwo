package ar.com.notarip.teocom.graphs.repository;

import org.springframework.data.repository.CrudRepository;

import ar.com.notarip.teocom.graphs.domain.DataSet;

public interface DataSetRepository extends CrudRepository<DataSet, Long>{
	
	DataSet findById(Long id);

}
