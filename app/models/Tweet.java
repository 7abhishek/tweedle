package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {
@Id
ObjectId _id;
@JsonProperty("created_at")
private String createdAt;
@JsonProperty("id")
private Integer id;
@JsonProperty("id_str")
private String idStr;
@JsonProperty("text")
private String text;
@JsonProperty("source")
private String source;
@JsonProperty("user")
private User user;
@JsonProperty("geo")
private Object geo;
@JsonProperty("coordinates")
private Object coordinates;
@JsonProperty("place")
private Object place;


public ObjectId get_id() {
    return _id;
}

public void set_id(ObjectId _id) {
    this._id = _id;
}

@JsonProperty("created_at")
public String getCreatedAt() {
return createdAt;
}

@JsonProperty("created_at")
public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

@JsonProperty("id")
public Integer getId() {
return id;
}

@JsonProperty("id")
public void setId(Integer id) {
this.id = id;
}

@JsonProperty("id_str")
public String getIdStr() {
return idStr;
}

@JsonProperty("id_str")
public void setIdStr(String idStr) {
this.idStr = idStr;
}

@JsonProperty("text")
public String getText() {
return text;
}

@JsonProperty("text")
public void setText(String text) {
this.text = text;
}

@JsonProperty("source")
public String getSource() {
return source;
}

@JsonProperty("source")
public void setSource(String source) {
this.source = source;
}



@JsonProperty("user")
public User getUser() {
return user;
}

@JsonProperty("user")
public void setUser(User user) {
this.user = user;
}

@JsonProperty("geo")
public Object getGeo() {
return geo;
}

@JsonProperty("geo")
public void setGeo(Object geo) {
this.geo = geo;
}

@JsonProperty("coordinates")
public Object getCoordinates() {
return coordinates;
}

@JsonProperty("coordinates")
public void setCoordinates(Object coordinates) {
this.coordinates = coordinates;
}

@JsonProperty("place")
public Object getPlace() {
return place;
}

@JsonProperty("place")
public void setPlace(Object place) {
this.place = place;
}

}