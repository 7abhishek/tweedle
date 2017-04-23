/**
 * 
 */
package dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import models.TweedleRequest;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;


import com.google.inject.Inject;

import dao.TweedleRequestDao;



public class TweedleRequestDaoImpl extends BasicDAO<TweedleRequest, ObjectId> implements TweedleRequestDao {

    @Inject
    protected TweedleRequestDaoImpl(Datastore ds) {
        super(ds);       
    }
//normal injection and constructor injection
    /* (non-Javadoc)
     * @see dao.TweedleRequestDao#saveRequest(models.TweedleRequest)
     */
    @SuppressWarnings("deprecation")
    @Override
    public TweedleRequest saveRequest(TweedleRequest tweedleRequest) {       
        if(getRequestByUserIdAndTweedle(tweedleRequest.getUserId(), tweedleRequest.getTweedle())==null){
            ds.save(tweedleRequest);
            //morphia- java object relation mapper for mongoDB 
            //ORM 
        }
        return tweedleRequest;
        //when ever u save object in a database the return type should be the updated object 
    }

    /* (non-Javadoc)
     * @see dao.TweedleRequestDao#getRequestsByUserId(java.lang.String)
     */
    @Override
    public List<TweedleRequest> getRequestsByUserId(String userId) {
        Query<TweedleRequest> query = createQuery().filter("userId", userId);
        return query != null ? query.asList(): java.util.Collections.EMPTY_LIST;
    }

    /* (non-Javadoc)
     * @see dao.TweedleRequestDao#getRequestByUserIdAndTweedle(java.lang.String, java.lang.String)
     */
    @Override
    public TweedleRequest getRequestByUserIdAndTweedle(String userId, String tweedle) {
        Query<TweedleRequest> query = createQuery().filter("userId", userId).filter("tweedle", tweedle);
        return query.get();
    }

}
