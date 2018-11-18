package fr.hes.raynaudmonitoring.backend.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import fr.hes.raynaudmonitoring.backend.backend.dao.CrisisRepository;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.CrisisDoc;

@Service
public class CrisisServiceImpl implements CrisisService {
	  @Autowired
	  private CrisisRepository crisisRepo;
	@Override
	public CrisisDoc create(CrisisDoc crisis) {
	    // We first search by email, the user should not exist

	    crisis.setId("129"); // internally we set the key with that id
	    return crisisRepo.save(crisis);
	}

	  @Override
	  public CrisisDoc findById(String id) {
	    final Optional<CrisisDoc> userObj = crisisRepo.findById(CrisisDoc.getKeyFor(id));
	    return userObj.orElseThrow();
	  }
	  
	  @Override
	  public List<CrisisDoc> findAll() {
	    return crisisRepo.findAll();
	  }

}
