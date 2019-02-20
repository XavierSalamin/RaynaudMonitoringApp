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
public class TreatmentDoc implements Serializable {
	private static final long serialVersionUID = 3072475211052736282L;
	protected static final String USER_KEY_PREFIX = "treatment::";

	@Id
	private String key;

	@Field
	private String id;
	@Field
	private String type;

	@Field
	private Integer day;

	@Field
	private Integer month;

	@Field
	private Integer year;

	@Field
	private Integer minute;

	@Field
	private Integer hour;

	@Field
	private boolean sideEffects;

	@Field
	private String userProfile;

	@Field
	private String description;

	public TreatmentDoc() {
	}

	public TreatmentDoc(final String key, final String id, final String type, final int day, final int month,
			final int year, final int minute, final int hour, final boolean sideEffects, final String userProfile,
			final String description) {
		super();
		this.key = key;
		this.id = id;
		this.type = type;
		this.day = day;
		this.month = month;
		this.year = year;
		this.minute = minute;

		this.hour = hour;
		this.sideEffects = sideEffects;

		this.userProfile = userProfile;

	}

	public TreatmentDoc(final String id, final String type) {
		this.id = id;
		this.type = type;
	}

	public static String getKeyFor(final String id) {
		return id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
		this.key = CrisisDoc.getKeyFor(id);
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

}
