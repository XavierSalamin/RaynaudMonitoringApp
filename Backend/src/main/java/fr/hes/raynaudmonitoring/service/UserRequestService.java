package fr.hes.raynaudmonitoring.service;



import java.util.List;

import javax.validation.Valid;

import fr.hes.raynaudmonitoring.dao.doc.UserProfileDoc;
import fr.hes.raynaudmonitoring.dao.doc.UserRequestDoc;
import fr.hes.raynaudmonitoring.dao.model.UserProfileJson;

public interface UserRequestService {
	UserRequestDoc findById(String id);

	List<UserRequestDoc> findAllUser();

	UserRequestDoc findByFirstName(String firstname);

	UserProfileDoc addUserProfile(@Valid UserProfileJson jsonUserProfile);

	UserProfileDoc findProfileById(String id);

	List<UserProfileDoc> findAllProfile();

	void activateUser(String firstname);
}
