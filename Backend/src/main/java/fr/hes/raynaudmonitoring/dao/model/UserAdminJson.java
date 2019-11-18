package fr.hes.raynaudmonitoring.dao.model;



import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonInclude;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({ "username", "password" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@Getter
@Setter
public class UserAdminJson {

	private String lastname;
	private String birthDate;

	// ***********************************************************************
	// CONSTRUCTORS
	// ***********************************************************************

	/**
	 * The empty constructor
	 */
	public UserAdminJson() {
	}

}
