package fr.hes.raynaudmonitoring.backend.backend.dao.doc;

import java.io.Serializable;

import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import lombok.Getter;
import lombok.Setter;


@Document
@Getter  @Setter
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
  private String startText;

  @Field
  private String endText;
  
  @Field
  private int day;
  @Field
  private int month;
  @Field
  private int year;
  
  public CrisisDoc() {}

  public CrisisDoc(String key, String id, String type, Integer hourStart, Integer hourEnd, String startText, int day, int month, int year) {
    super();
    this.key = key;
    this.id = id;
    this.type = type;
    this.hourStart = hourStart;
    this.hourEnd = hourEnd;
    this.startText = startText;
    this.day= day;
    this.month = month;
    this.year =year;
  }

  public CrisisDoc(String id, String type) {
    this.id = id;
    this.type = type;
  }

  public static String getKeyFor(String id) {
    return  id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
    this.key = CrisisDoc.getKeyFor(id);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }



  public Integer getHourStart() {
    return hourStart;
  }

  public void setHourStart(Integer hourStart) {
    this.hourStart = hourStart;
  }





}

