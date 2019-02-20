package fr.hes.raynaudmonitoring.backend.backend.dao;

import java.util.List;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;

import fr.hes.raynaudmonitoring.backend.backend.dao.doc.TreatmentDoc;

public interface TreatmentRepository extends CrudRepository<TreatmentDoc, String> {

	@Override
	@Query("#{#n1ql.selectEntity} WHERE type = 'treatment'")
	List<TreatmentDoc> findAll();

}
