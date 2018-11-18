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



  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((hourStart == null) ? 0 : hourStart.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CrisisDoc other = (CrisisDoc) obj;
    if (hourStart == null) {
      if (other.hourStart != null)
        return false;
    } else if (!hourStart.equals(other.hourStart))
      return false;

    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (key == null) {
      if (other.key != null)
        return false;
    } else if (!key.equals(other.key))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;

    return true;
  }

}

