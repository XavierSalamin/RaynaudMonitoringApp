package fr.hes.raynaudmonitoring.backend.backend.web.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserRequestDoc;
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
	  
		
	  @GetMapping(value = "/find/{firstname}")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public UserRequestDoc findByFirstName(@PathVariable String firstname) {
	    logger.info("Find user by id: {}", firstname);
	    return userRequestService.findByFirstName(firstname);
	  }

	  @GetMapping(value = "/find/all")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public List<UserRequestDoc> findAllUser() {

	    return userRequestService.findAllUser();
	  }
}
