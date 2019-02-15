package fr.hes.raynaudmonitoring.backend.backend.service;

import java.util.List;

import fr.hes.raynaudmonitoring.backend.backend.dao.doc.CrisisDoc;

public interface CrisisService {

	CrisisDoc create(CrisisDoc crisis);

	CrisisDoc findById(String id);

	List<CrisisDoc> findAll();

	List<CrisisDoc> findCrisisByUser(String profileId);

}
