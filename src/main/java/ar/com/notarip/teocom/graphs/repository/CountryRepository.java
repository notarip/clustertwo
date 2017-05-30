package ar.com.notarip.teocom.graphs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ar.com.notarip.teocom.graphs.domain.Country;

public interface CountryRepository extends CrudRepository<Country, Long>{

	List<Country> findAll();
	Country findById(Long id);
	
}
