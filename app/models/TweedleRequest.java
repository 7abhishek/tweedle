/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author abhishek
 *
 */
@Entity(value = "tweedleRequest", noClassnameStored = true)
public class TweedleRequest {
    @Id
    ObjectId _id;
    String userId;
    String tweedle;
    String trackTerms;
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
}
