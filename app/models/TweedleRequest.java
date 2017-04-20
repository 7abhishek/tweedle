package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author abhishek
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity(value = "tweedleRequest", noClassnameStored = true)
public class TweedleRequest {
    @Id
//<<<<<<< 22ec93839ac9efabd6c49f919c03efed37a4e7ba
    @JsonIgnore

    ObjectId _id;//mongo db will have the ObjectId

    String userId;
    String tweedle;
    String trackTerms;
    TweedleStatus status = TweedleStatus.CREATED;
    Boolean notify=false;
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTweedle() {
        return tweedle;
    }
    public void setTweedle(String tweedle) {
        this.tweedle = tweedle;
    }
    public String getTrackTerms() {
        return trackTerms;
    }
    public void setTrackTerms(String trackTerms) {
        this.trackTerms = trackTerms;
    }
    public ObjectId get_id() {
        return _id;
    }
    public void set_id(ObjectId _id) {
        this._id = _id;
    }
    public TweedleStatus getStatus() {
        return status;
    }
    public void setStatus(TweedleStatus status) {
        this.status = status;
    }
    public Boolean getNotify() {
        return notify;
    }
    public void setNotify(Boolean notify) {
        this.notify = notify;
    }    
    
}
