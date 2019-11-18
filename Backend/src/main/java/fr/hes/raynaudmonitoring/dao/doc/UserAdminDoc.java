package fr.hes.raynaudmonitoring.dao.doc;



import java.io.Serializable;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * User Admin for login to the raynaud monitoring front
 *
 * @author Xavier Salamin
 *
 */
@Document
@Getter
@Setter
public class UserAdminDoc implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -941242033616469975L;

	/** The Constant USER_KEY_PREFIX. */
	protected static final String USER_KEY_PREFIX = "user-admin::";

	/** The key. */
	@Id
	private String key;

	/** The id. */
	@Field
	private String id;

	/** The type. */
	@Field
	private String type;

	/** The username. */
	@Field
	private String username;

	/** The password. */
	@Field
	private String password;

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(final String id) {
		this.id = id;
		this.key = getKeyFor(id);
	}

	/**
	 * Gets the key for.
	 *
	 * @param id the id
	 * @return the key for
	 */
	public static String getKeyFor(final String id) {
		return id;
	}

	/**
	 * Instantiates a new user admin.
	 */
	public UserAdminDoc() {
	}

	/**
	 * Instantiates a new user admin.
	 *
	 * @param id       the id
	 * @param key      the key
	 * @param username the username
	 * @param password the password
	 */
	public UserAdminDoc(final String id, final String key, final String type, final String username,
			final String password) {
		super();
		this.id = id;
		this.key = key;
		this.type = type;
		this.username = username;
		this.password = password;
	}

}
