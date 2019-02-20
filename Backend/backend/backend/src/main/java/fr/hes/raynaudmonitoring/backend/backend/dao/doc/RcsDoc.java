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
public class RcsDoc implements Serializable {
	private static final long serialVersionUID = 307247521105573212L;
	protected static final String USER_KEY_PREFIX = "rcs::";

	@Id
	private String key;

	@Field
	private String id;
	@Field
	private String type;

	@Field
	private int day;
	@Field
	private int month;
	@Field
	private int year;

	@Field
	private String userProfile;

	@Field
	private int rcs;

	public RcsDoc() {

	}

	public RcsDoc(final String key, final String id, final String type, final int day, final int month, final int year,
			final String userProfile, final int rcs) {
		this.key = key;
		this.id = id;
		this.type = type;
		this.day = day;
		this.month = month;
		this.year = year;
		this.userProfile = userProfile;
		this.rcs = rcs;
	}

	public RcsDoc(final String id, final String type) {
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
}
