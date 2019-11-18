package fr.hes.raynaudmonitoring.dao;



import java.util.List;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import fr.hes.raynaudmonitoring.dao.doc.UserAdminDoc;



@CrossOrigin(origins = "http://localhost:4200")
public interface UserAdminRepository extends CrudRepository<UserAdminDoc, String> {
	@Query("#{#n1ql.selectEntity} WHERE type = 'user_admin'")
	List<UserAdminDoc> findAllUserAdmin();

}
