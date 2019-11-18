package fr.hes.raynaudmonitoring.dao;



import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import fr.hes.raynaudmonitoring.dao.doc.UserRequestDoc;


@CrossOrigin(origins = "http://localhost:4200")
public interface UserRequestRepository  extends CrudRepository<UserRequestDoc, String> {
    @Query("#{#n1ql.selectEntity} WHERE type = 'user_request'")
	  List<UserRequestDoc> findAllUserRequest();


	  @Query("#{#n1ql.selectEntity} WHERE firstname = $1")
		Optional<UserRequestDoc> findByFirstname(String firstname);
}
