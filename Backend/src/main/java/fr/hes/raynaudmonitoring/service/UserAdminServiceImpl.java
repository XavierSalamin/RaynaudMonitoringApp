package fr.hes.raynaudmonitoring.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.hes.raynaudmonitoring.dao.UserAdminRepository;

@Service
public class UserAdminServiceImpl extends UserAdminService {

	@Autowired
	private UserAdminRepository userAdminRepository;

}
