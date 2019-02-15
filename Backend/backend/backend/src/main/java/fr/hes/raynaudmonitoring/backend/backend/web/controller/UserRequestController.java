package fr.hes.raynaudmonitoring.backend.backend.web.controller;


import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserProfileDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserRequestDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.model.UserProfileJson;
import fr.hes.raynaudmonitoring.backend.backend.service.UserRequestService;

@RequestMapping("/user-request")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserRequestController {
	
	protected static final Logger logger = LoggerFactory.getLogger(UserRequestController.class);
	
	 @Autowired
	 private UserRequestService userRequestService;
	
	  @GetMapping(value = "/{userId}")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public UserRequestDoc findById(@PathVariable String userId) {
	    logger.info("Find user by id: {}", userId);
	    return userRequestService.findById(userId);
	  }
	  
	  @GetMapping(value = "/profile/{userId}")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public UserProfileDoc findProfileById(@PathVariable String userId) {
	    logger.info("Find user by id: {}", userId);
	    return userRequestService.findProfileById(userId);
	  }
	  
		
	  @GetMapping(value = "/find/{firstname}")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public UserRequestDoc findByFirstName(@PathVariable String firstname) {
	    logger.info("Find user by id: {}", firstname);
	    return userRequestService.findByFirstName(firstname);
	  }
	  
	  
		
	  @PostMapping(value = "/add")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public UserProfileDoc  addUserProfile(@RequestBody @Valid final UserProfileJson jsonUserProfile) {
	    logger.info("Add user profile");
	    return userRequestService.addUserProfile(jsonUserProfile);
	  }

	  @GetMapping(value = "/find/all")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public List<UserRequestDoc> findAllUser() {

	    return userRequestService.findAllUser();
	  }
	  
	  
	  @GetMapping(value = "/profile/all")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public List<UserProfileDoc> findAllProfile() {

	    return userRequestService.findAllProfile();
	  }
}
