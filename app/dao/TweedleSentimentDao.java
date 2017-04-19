/**
 * 
 */
package dao;

import models.TweedleRequest;
import models.TweetSentiment;

/**
 * @author abhishek
 *
 */

public interface TweedleSentimentDao {
        public TweetSentiment saveTweedleSentiment(TweetSentiment sentiment);
        public TweetSentiment getTweedleSentiment(TweedleRequest request);
}
