package fr.hes.raynaudmonitoring.backend.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.hes.raynaudmonitoring.backend.backend.dao.UserRequestRepository;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.CrisisDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserRequestDoc;
@Service
public class UserRequestServiceImpl implements UserRequestService {
	  @Autowired
	  private UserRequestRepository userRequestRepo;
	  @Override
	  public UserRequestDoc findById(String id) {
	    final Optional<UserRequestDoc> userObj = userRequestRepo.findById(UserRequestDoc.getKeyFor(id));
	    return userObj.orElseThrow();
	  }
	  
	  @Override
	  public UserRequestDoc findByFirstName(String firstname) {
	    final Optional<UserRequestDoc> userObj = userRequestRepo.findByFirstname(firstname);
	    return userObj.orElseThrow();
	  }

	  @Override
	  	public List<UserRequestDoc> findAllUser(){
		  return userRequestRepo.findAllUserRequest();
	  }
}
