/**
 * 
 */
package providers;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.inject.Provider;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class DefaultDataStoreProvider implements Provider<Datastore>{

    /* (non-Javadoc)
     * @see com.google.inject.Provider#get()
     */
    @Override
    public Datastore get() {        
        final Morphia morphia = new Morphia();
        final Datastore datastore = morphia.createDatastore(new MongoClient("localhost" , 27017 ), "morphia_example");
        datastore.ensureIndexes();
        return datastore;
    }

}
