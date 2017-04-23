/**
 * 
 */
package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;


@Entity(value = "tweedleSentiment", noClassnameStored = true)
public class TweetSentiment {
    @Id
    ObjectId _id;
    TweedleRequest request;
    Integer tweetId;
    Sentiment sentiment;
    public TweedleRequest getRequest() {
        return request;
    }
    public void setRequest(TweedleRequest request) {
        this.request = request;
    }
    public Integer getTweetId() {
        return tweetId;
    }
    public void setTweetId(Integer tweetId) {
        this.tweetId = tweetId;
    }
    public Sentiment getSentiment() {
        return sentiment;
    }
    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }
    
    
}
