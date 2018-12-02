package fr.hes.raynaudmonitoring.backend.backend.service;


import java.util.List;
import java.util.Optional;

import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserRequestDoc;



public interface UserRequestService {
	UserRequestDoc findById(String id);
	List <UserRequestDoc> findAllUser();
	UserRequestDoc findByFirstName(String firstname);
}
