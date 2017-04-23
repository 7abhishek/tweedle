/**
 * 
 */
package dao.impl;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import dao.TweetDao;
import models.Tweet;


public class TweetDaoImpl extends BasicDAO<Tweet, ObjectId> implements TweetDao{

    private Logger logger = LoggerFactory.getLogger(TweetDaoImpl.class);
    @Inject
    protected TweetDaoImpl(Datastore ds) {
        super(ds);       
    }

    @Override
    public Tweet saveTweet(Tweet tweet) {        
        ds.save(tweet);
        return tweet;
    }
    
    

}
