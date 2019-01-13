package fr.hes.raynaudmonitoring.backend.backend.service;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserProfileDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserRequestDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.model.UserProfileJson;



public interface UserRequestService {
	UserRequestDoc findById(String id);
	List <UserRequestDoc> findAllUser();
	UserRequestDoc findByFirstName(String firstname);
	
	UserProfileDoc addUserProfile(@Valid UserProfileJson jsonUserProfile);
}
