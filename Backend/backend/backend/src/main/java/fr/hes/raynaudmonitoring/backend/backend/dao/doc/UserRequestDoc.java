package fr.hes.raynaudmonitoring.backend.backend.dao.doc;

import java.io.Serializable;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
public class UserRequestDoc implements Serializable {

	private static final long serialVersionUID = 3072475211055416282L;
	protected static final String USER_KEY_PREFIX = "userRequest::";

	@Id
	private String id;

	@Field
	private String firstname;
	@Field
	private String lastname;
	@Field
	private String birthDate;
	@Field
	private Boolean isActivated;

	public UserRequestDoc(final String id, final String firstname, final String lastname, final String birthDate,
			final Boolean isActivated) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthDate = birthDate;
		this.isActivated = false;

	}

	public static String getKeyFor(final String id) {
		return id;
	}

}
