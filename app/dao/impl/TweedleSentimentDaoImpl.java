/**
 * 
 */
package dao.impl;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.google.inject.Inject;

import dao.TweedleSentimentDao;
import models.TweedleRequest;
import models.TweetSentiment;

public class TweedleSentimentDaoImpl extends BasicDAO<TweetSentiment, ObjectId> implements TweedleSentimentDao{
       
    @Inject
    protected TweedleSentimentDaoImpl(Datastore ds) {
        super(ds);        
    }


    @Override
    public TweetSentiment saveTweedleSentiment(TweetSentiment sentiment) {
        Query<TweetSentiment> updateQuery  = createQuery();
        updateQuery.filter("request", sentiment.getRequest());
        @SuppressWarnings("deprecation")
        UpdateOperations<TweetSentiment> ops = ds
                .createUpdateOperations(TweetSentiment.class)
                .set("sentiment", sentiment.getSentiment());
        ds.update(updateQuery, ops, true);        
        return sentiment;
        
    }

    
    @Override
    public TweetSentiment getTweedleSentiment(TweedleRequest request) {
        Query<TweetSentiment> query  = createQuery();
        query.filter("request", request);
        return query.get();
    }

}
