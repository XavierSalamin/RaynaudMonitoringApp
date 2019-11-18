package fr.hes.raynaudmonitoring.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.hes.raynaudmonitoring.dao.CrisisRepository;
import fr.hes.raynaudmonitoring.dao.doc.CrisisDoc;

@Service
public class CrisisServiceImpl implements CrisisService {
	@Autowired
	private CrisisRepository crisisRepo;
	@Override
	public CrisisDoc create(CrisisDoc crisis) {

		return crisisRepo.save(crisis);
	}

	@Override
	public CrisisDoc findById(String id) {
		final Optional<CrisisDoc> userObj = crisisRepo.findById(CrisisDoc.getKeyFor(id));
		return userObj.orElseThrow(null);
	}

	@Override
	public List<CrisisDoc> findAll() {
		return crisisRepo.findAll();
	}

	@Override
	public List<CrisisDoc> findCrisisByUser(String profileId) {
		// TODO Auto-generated method stub
		return crisisRepo.findCrisisById(profileId);
	}

}
