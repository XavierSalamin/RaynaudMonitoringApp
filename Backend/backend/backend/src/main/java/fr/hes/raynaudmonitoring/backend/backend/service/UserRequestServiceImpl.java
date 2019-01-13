package fr.hes.raynaudmonitoring.backend.backend.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.hes.raynaudmonitoring.backend.backend.dao.UserProfileRepository;
import fr.hes.raynaudmonitoring.backend.backend.dao.UserRequestRepository;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.CrisisDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserProfileDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserRequestDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.model.UserProfileJson;
@Service
public class UserRequestServiceImpl implements UserRequestService {
	  @Autowired
	  private UserRequestRepository userRequestRepo;
	  @Autowired
	  private UserProfileRepository userProfileRepo;
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
	  
	  @Override
	public UserProfileDoc addUserProfile(@Valid UserProfileJson jsonUserProfile) {
		  UserProfileDoc userProfile = mapJsontoUserProfile(jsonUserProfile);
		  
		    return userProfileRepo.save(userProfile);
	  }

	private UserProfileDoc mapJsontoUserProfile(@Valid UserProfileJson jsonUserProfile) {

		UserProfileDoc userProfile = new UserProfileDoc();
		userProfile.setType("user_profile");
		userProfile.setId(jsonUserProfile.getLastname()+jsonUserProfile.getLastname()+jsonUserProfile.getBirthDate());
		
		userProfile.setBirthDate(jsonUserProfile.getBirthDate());
		userProfile.setFirstname(jsonUserProfile.getFirstname());
		userProfile.setLastname(jsonUserProfile.getLastname());
		
		//userProfile.setId("21131");
		userProfile.setPatientNumber(jsonUserProfile.getPatientNumber());
		userProfile.setCycleNumber(jsonUserProfile.getCycleNumber());
		userProfile.setKitUsed(jsonUserProfile.getKitUsed());
		userProfile.setRandomisationNumber(jsonUserProfile.getRandomisationNumber());
		userProfile.setPeriodDuration(jsonUserProfile.getPeriodDuration());
		userProfile.setPeriodNumber(jsonUserProfile.getPatientNumber());
		
		return userProfile;
	}


}
