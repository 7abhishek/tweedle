/**
 * 
 */
package dao;

import models.TweedleRequest;
import models.TweetSentiment;


public interface TweedleSentimentDao {
        public TweetSentiment saveTweedleSentiment(TweetSentiment sentiment);
        public TweetSentiment getTweedleSentiment(TweedleRequest request);
}
