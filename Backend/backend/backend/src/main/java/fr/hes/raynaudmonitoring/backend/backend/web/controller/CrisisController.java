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



import fr.hes.raynaudmonitoring.backend.backend.dao.doc.CrisisDoc;

import fr.hes.raynaudmonitoring.backend.backend.service.CrisisService;





@RequestMapping("/crisis")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CrisisController {
	
	
	protected static final Logger logger = LoggerFactory.getLogger(CrisisController.class);

	  @Autowired
	  private CrisisService crisisService;


	  @PostMapping(value = "")
	  @ResponseStatus(HttpStatus.CREATED)
	  @ResponseBody
	  public CrisisDoc create(@Valid @RequestBody CrisisDoc crisis) {
	    logger.info("Create user");
	    return crisisService.create(crisis);
	  }



	  @GetMapping(value = "/{userId}")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public CrisisDoc findById(@PathVariable String userId) {
	    logger.info("Find user by id: {}", userId);
	    return crisisService.findById(userId);
	  }
	  
	  @GetMapping(value = "/find/all")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public List<CrisisDoc> findAll() {
	    logger.info("Find all users");
	    logger.info("Il se passe des choses");
	    return crisisService.findAll();
	  }
	  
	  
	  @GetMapping(value = "/find/{id}")
	  @ResponseStatus(HttpStatus.OK)
	  @ResponseBody
	  public List<CrisisDoc> findCrisisById(@PathVariable String id){
		  logger.info("find crisis for the specific users");
		  return crisisService.findCrisisByUser(id);
	  }


}
