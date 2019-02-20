package fr.hes.raynaudmonitoring.backend.backend.dao;

import java.util.List;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserProfileDoc;

@CrossOrigin(origins = "http://localhost:4200")
public interface UserProfileRepository extends CrudRepository<UserProfileDoc, String> {

	@Query("#{#n1ql.selectEntity} WHERE type = 'user_profile'")
	List<UserProfileDoc> findAllUserProfile();

}
