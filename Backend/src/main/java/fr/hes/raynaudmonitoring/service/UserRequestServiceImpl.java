package fr.hes.raynaudmonitoring.service;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.hes.raynaudmonitoring.dao.UserProfileRepository;
import fr.hes.raynaudmonitoring.dao.UserRequestRepository;
import fr.hes.raynaudmonitoring.dao.doc.UserProfileDoc;
import fr.hes.raynaudmonitoring.dao.doc.UserRequestDoc;
import fr.hes.raynaudmonitoring.dao.model.UserProfileJson;

@Service
public class UserRequestServiceImpl implements UserRequestService {

	protected static final Logger logger = LoggerFactory.getLogger(UserRequestServiceImpl.class);
	@Autowired
	private UserRequestRepository userRequestRepo;
	@Autowired
	private UserProfileRepository userProfileRepo;

	@Override
	public UserRequestDoc findById(final String id) {
		final Optional<UserRequestDoc> userObj = userRequestRepo.findById(UserRequestDoc.getKeyFor(id));
		return userObj.orElseThrow(null);
	}

	@Override
	public UserProfileDoc findProfileById(final String id) {
		final Optional<UserProfileDoc> userObj = userProfileRepo.findById(UserProfileDoc.getKeyFor(id));
		return userObj.orElseThrow(null);
	}

	@Override
	public UserRequestDoc findByFirstName(final String firstname) {
		final Optional<UserRequestDoc> userObj = userRequestRepo.findByFirstname(firstname);
		return userObj.orElseThrow(null);
	}

	@Override
	public List<UserRequestDoc> findAllUser() {
		// On retourne seulement les joueurs qui n'ont pas été activé

		final List<UserRequestDoc> userList = userRequestRepo.findAllUserRequest();
		return userList.stream().filter(user -> user != null).filter(user -> user.getIsActivated() == false)
				.collect(Collectors.toList());
	}

	@Override
	public UserProfileDoc addUserProfile(@Valid final UserProfileJson jsonUserProfile) {
		final UserProfileDoc userProfile = mapJsontoUserProfile(jsonUserProfile);

		return userProfileRepo.save(userProfile);
	}

	private UserProfileDoc mapJsontoUserProfile(@Valid final UserProfileJson jsonUserProfile) {

		final UserProfileDoc userProfile = new UserProfileDoc();
		userProfile.setType("user_profile");
		userProfile
		.setId(jsonUserProfile.getLastname() + jsonUserProfile.getLastname() + jsonUserProfile.getBirthDate());

		userProfile.setBirthDate(jsonUserProfile.getBirthDate());
		userProfile.setFirstname(jsonUserProfile.getFirstname());
		userProfile.setLastname(jsonUserProfile.getLastname());

		// userProfile.setId("21131");
		userProfile.setPatientNumber(jsonUserProfile.getPatientNumber());
		userProfile.setCycleNumber(jsonUserProfile.getCycleNumber());
		userProfile.setKitUsed(jsonUserProfile.getKitUsed());
		userProfile.setRandomisationNumber(jsonUserProfile.getRandomisationNumber());
		userProfile.setPeriodDuration(jsonUserProfile.getPeriodDuration());
		userProfile.setPeriodNumber(jsonUserProfile.getPeriodNumber());

		return userProfile;
	}

	@Override
	public List<UserProfileDoc> findAllProfile() {
		return userProfileRepo.findAllUserProfile();
	}

	public String getProfileIdByPatientNumber(final String patientNumber) {
		final Optional<UserProfileDoc> user = userProfileRepo.findByPatientNumber(patientNumber);

		final String result;
		if (user.isPresent()) {
			result = user.get().getLastname() + user.get().getFirstname();
		} else {
			logger.info("Didn't found a User Profile for this patient number {}", patientNumber);
			result = null;
		}
		return result;
	}

	public UserProfileDoc getProfileByPatientNumber(final String patientNumber) {
		final Optional<UserProfileDoc> user = userProfileRepo.findByPatientNumber(patientNumber);

		if(user.isPresent()) {
			return user.get();
		}else {
			return null;
		}
	}

	// Get a userRequest and set is variable isActivated at true
	@Override
	public void activateUser(final String firstname) {
		final Optional<UserRequestDoc> userObja = userRequestRepo.findByFirstname(firstname);

		// On a besoin d'avoir l'id pour écraser correctement l'objet
		final Optional<UserRequestDoc> userObj = userRequestRepo.findById(userObja.get().getId());
		if (userObj.isPresent()) {
			if (userObj.get().getIsActivated() != null)
				userObj.get().setIsActivated(true);
			logger.info("Activating user : " + firstname);
		}

		userRequestRepo.save(userObj.get());
	}
}
