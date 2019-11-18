package fr.hes.raynaudmonitoring.dao;


import java.util.List;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.hes.raynaudmonitoring.dao.doc.CrisisDoc;



public interface CrisisRepository extends CrudRepository<CrisisDoc, String> {
	@Override
	@Query("#{#n1ql.selectEntity} WHERE type = 'crisis'")
	List<CrisisDoc> findAll();

	@Query("#{#n1ql.selectEntity} WHERE type = 'crisis' AND user_profile = $userId")
	List<CrisisDoc> findCrisisById(@Param("userId") String userId);

}
