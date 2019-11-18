package fr.hes.raynaudmonitoring.dao.doc;



import java.io.Serializable;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UserProfileDoc.
 */
@Document
@Getter
@Setter
public class UserProfileDoc implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3072475211055736282L;

	/** The Constant USER_KEY_PREFIX. */
	protected static final String USER_KEY_PREFIX = "user-profile::";

	/** The key. */
	@Id
	private String key;

	/** The id. */
	@Field
	private String id;

	/** The type. */
	@Field
	private String type;

	/** The birth date. */
	@Field
	private String birthDate;

	/** The firstname. */
	@Field
	private String firstname;

	/** The lastname. */
	@Field
	private String lastname;

	/** The patient number. */
	@Field
	private String patientNumber;

	/** The randomisation number. */
	@Field
	private Integer randomisationNumber;

	/** The cycle number. */
	@Field
	private Integer cycleNumber;

	/** The period number. */
	@Field
	private Integer periodNumber;

	/** The period duration. */
	@Field
	private Integer periodDuration;

	/** The kit used. */
	@Field
	private String kitUsed;

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
	 * Instantiates a new user profile doc.
	 */
	public UserProfileDoc() {
	}

	/**
	 * Instantiates a new user profile doc.
	 *
	 * @param id                  the id
	 * @param type                the type
	 * @param randomisationNumber the randomisation number
	 * @param patientNumber       the patient number
	 * @param cycleNumber         the cycle number
	 * @param periodNumber        the period number
	 * @param periodDuration      the period duration
	 * @param kitUsed             the kit used
	 * @param birthDate           the birth date
	 * @param firstname           the firstname
	 * @param lastname            the lastname
	 */
	public UserProfileDoc(final String id, final String type, final Integer randomisationNumber,
			final String patientNumber, final Integer cycleNumber, final Integer periodNumber,
			final Integer periodDuration, final String kitUsed, final String birthDate, final String firstname,
			final String lastname) {
		super();
		this.id = id;
		this.type = type;
		this.patientNumber = patientNumber;
		this.randomisationNumber = randomisationNumber;
		this.cycleNumber = cycleNumber;
		this.periodNumber = periodNumber;
		this.periodDuration = periodDuration;
		this.kitUsed = kitUsed;
		this.birthDate = birthDate;
		this.firstname = firstname;
		this.lastname = lastname;
	}
}
