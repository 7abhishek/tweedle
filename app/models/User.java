/**
 * 
 */
package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

@JsonProperty("id")
private Integer id;
@JsonProperty("id_str")
private String idStr;
@JsonProperty("name")
private String name;
@JsonProperty("screen_name")
private String screenName;
@JsonProperty("location")
private Object location;
@JsonProperty("url")
private Object url;
@JsonProperty("description")
private String description;
@JsonProperty("protected")
private Boolean _protected;
@JsonProperty("verified")
private Boolean verified;
public Integer getId() {
    return id;
}
public void setId(Integer id) {
    this.id = id;
}
public String getIdStr() {
    return idStr;
}
public void setIdStr(String idStr) {
    this.idStr = idStr;
}
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public String getScreenName() {
    return screenName;
}
public void setScreenName(String screenName) {
    this.screenName = screenName;
}
public Object getLocation() {
    return location;
}
public void setLocation(Object location) {
    this.location = location;
}
public Object getUrl() {
    return url;
}
public void setUrl(Object url) {
    this.url = url;
}
public String getDescription() {
    return description;
}
public void setDescription(String description) {
    this.description = description;
}
public Boolean get_protected() {
    return _protected;
}
public void set_protected(Boolean _protected) {
    this._protected = _protected;
}
public Boolean getVerified() {
    return verified;
}
public void setVerified(Boolean verified) {
    this.verified = verified;
}

}
