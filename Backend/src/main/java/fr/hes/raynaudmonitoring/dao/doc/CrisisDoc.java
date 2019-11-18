package fr.hes.raynaudmonitoring.dao.doc;



import java.io.Serializable;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
public class CrisisDoc implements Serializable {

	private static final long serialVersionUID = 3072475211055736282L;
	protected static final String USER_KEY_PREFIX = "crisis::";

	@Id
	private String key;

	@Field
	private String id;
	@Field
	private String type;

	@Field
	private Integer hourStart;

	@Field
	private Integer hourEnd;

	@Field
	private Integer minuteEnd;
	@Field
	private Integer minuteStart;

	@Field
	private String startText;

	@Field
	private String startName;

	@Field
	private String endName;

	@Field
	private String endText;

	@Field
	private String userProfile;

	@Field
	private int pain;

	@Field
	private int day;
	@Field
	private int month;
	@Field
	private int year;

	public CrisisDoc() {
	}

	public CrisisDoc(final String key, final String id, final String type, final Integer hourStart,
			final Integer hourEnd, final String startText, final int day, final int month, final int year,
			final String userProfile, final int pain, final int minuteEnd,
			final int minuteStart, final String startName, final String endName) {
		super();
		this.key = key;
		this.id = id;
		this.type = type;
		this.hourStart = hourStart;
		this.hourEnd = hourEnd;
		this.startText = startText;
		this.day = day;
		this.month = month + 1;
		this.userProfile = userProfile;
		this.year = year;
		this.pain = pain;
		this.startName = startName;
		this.endName = endName;

		this.minuteEnd = minuteEnd;
		this.minuteStart = minuteStart;
	}

	public CrisisDoc(final String id, final String type) {
		this.id = id;
		this.type = type;
	}

	public void setMonth(final int month) {
		this.month = month + 1;
	}

	public int getMonth() {
		return month;
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

	public Integer getHourStart() {
		return hourStart;
	}

	public void setHourStart(final Integer hourStart) {
		this.hourStart = hourStart;
	}



}
