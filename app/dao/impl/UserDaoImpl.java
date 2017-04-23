/**
 * 
 */
package dao.impl;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.google.inject.Inject;

import models.TweedleUser;
import dao.UserDao;


public class UserDaoImpl extends BasicDAO<TweedleUser,ObjectId> implements UserDao {

    
    @Inject
    protected UserDaoImpl(Datastore ds) {
        super(ds);       
    }

    @SuppressWarnings("deprecation")
    @Override
    public TweedleUser addUser(TweedleUser user) {
        ds.save(user);
        return user;
    }

}
