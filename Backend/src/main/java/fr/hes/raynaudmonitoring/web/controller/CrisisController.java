package fr.hes.raynaudmonitoring.web.controller;



import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import fr.hes.raynaudmonitoring.dao.doc.CrisisDoc;
import fr.hes.raynaudmonitoring.service.CrisisService;
import fr.hes.raynaudmonitoring.service.ExcelService;

@RequestMapping("/crisis")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CrisisController {

	protected static final Logger logger = LoggerFactory.getLogger(CrisisController.class);

	@Autowired
	private CrisisService crisisService;

	@Autowired
	private ExcelService excelService;

	@PostMapping(value = "")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public CrisisDoc create(@Valid @RequestBody final CrisisDoc crisis) {
		logger.info("Create user");
		return crisisService.create(crisis);
	}

	@GetMapping(value = "/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CrisisDoc findById(@PathVariable final String userId) {
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
	public List<CrisisDoc> findCrisisById(@PathVariable final String id) {
		logger.info("find crisis for the specific users");
		return crisisService.findCrisisByUser(id);
	}

	@GetMapping(value = "/excel/{patientNumber}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<StreamingResponseBody> generateExcelById(@PathVariable final String patientNumber,
			final HttpServletResponse response) {
		logger.info("Trying to generate excel");
		;
		return excelService.generateExcelById(patientNumber, response);

	}

}
