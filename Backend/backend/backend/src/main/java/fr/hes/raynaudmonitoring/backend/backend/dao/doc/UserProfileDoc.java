package fr.hes.raynaudmonitoring.backend.backend.dao.doc;

import java.io.Serializable;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter  @Setter
public class UserProfileDoc  implements Serializable{
	  private static final long serialVersionUID = 3072475211055736282L;
	  protected static final String USER_KEY_PREFIX = "user-profile::";

	  @Id
	  private String key;

	  @Field
	  private String id;
	  @Field
	  private String type;

	  
	  @Field
	  private String birthDate;
	  @Field
	  private String firstname;
	  
	  @Field
	  private String lastname;

	  
	  
	  @Field
	  private Integer patientNumber;

	  @Field
	  private Integer randomisationNumber;

	  @Field
	  private Integer cycleNumber;
	  
	  @Field
	  private Integer periodNumber;
	  
	  @Field
	  private Integer periodDuration;
	  
	  @Field
	  private String kitUsed;
	  

		  public void setId(String id) {
		    this.id = id;
		    this.key = getKeyFor(id);
		  }
		  public static String getKeyFor(String id) {
			    return  id;
			  }
	  public UserProfileDoc() {}



	  public UserProfileDoc(String id, String type, Integer randomisationNumber, Integer patientNumber, Integer cycleNumber, Integer periodNumber, Integer periodDuration, String kitUsed, String birthDate, String firstname, String lastname) {
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
