package fr.hes.raynaudmonitoring.backend.backend.dao;

import java.util.List;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;



import fr.hes.raynaudmonitoring.backend.backend.dao.doc.CrisisDoc;



public interface CrisisRepository extends CrudRepository<CrisisDoc, String>  {
    @Query("#{#n1ql.selectEntity} WHERE type = 'crisis'")
	  List<CrisisDoc> findAll();
}
