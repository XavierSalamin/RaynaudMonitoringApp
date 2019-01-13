package fr.hes.raynaudmonitoring.backend.backend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserProfileDoc;



@CrossOrigin(origins = "http://localhost:4200")
public interface UserProfileRepository  extends CrudRepository<UserProfileDoc, String> {

}
