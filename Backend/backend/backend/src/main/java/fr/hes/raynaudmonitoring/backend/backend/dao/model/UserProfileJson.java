package fr.hes.raynaudmonitoring.backend.backend.dao.model;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonInclude;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

/**
 * User request json object
 *
 * @author xsalamin
 */
@JsonPropertyOrder({"firstname", "lastname", "birthDate", "patientNumber", "randomisationNUmber", "cycleNumber", "periodNumber", "periodDuration", "kitUsed"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@Getter @Setter
public class UserProfileJson {

	private Integer patientNumber;
	private Integer randomisationNumber;
	private Integer cycleNumber;
	private Integer periodNumber;
	private Integer periodDuration;
	private String kitUsed;
	
	
	private String firstname;
	private String lastname;
	private String birthDate;
	

	
	// ***********************************************************************
	// CONSTRUCTORS
	// ***********************************************************************

	/**
	 * The empty constructor
	 */
	public UserProfileJson() {
	}


}