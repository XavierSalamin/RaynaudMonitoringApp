package fr.hes.raynaudmonitoring.dao;


import java.util.List;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;

import fr.hes.raynaudmonitoring.dao.doc.RcsDoc;



public interface RcsRepository extends CrudRepository<RcsDoc, String> {
	@Override
	@Query("#{#n1ql.selectEntity} WHERE type = 'rcs'")
	List<RcsDoc> findAll();
}
