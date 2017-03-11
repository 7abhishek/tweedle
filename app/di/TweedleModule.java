/**
 * 
 */
package di;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import producers.KafkaProducerImpl;
import producers.Kafkaproducer;
import providers.DefaultDataStoreProvider;
import services.Notifier;
import services.impl.NotifierImpl;

import com.google.inject.AbstractModule;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * @author abhishek
 *
 */
public class TweedleModule extends AbstractModule {

    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    Logger logger  = LoggerFactory.getLogger(TweedleModule.class);
    @Override
    protected void configure() {        
        bind(Notifier.class).to(NotifierImpl.class);
        bind(Kafkaproducer.class).to(KafkaProducerImpl.class);
        bind(Datastore.class).toProvider(DefaultDataStoreProvider.class);
        logger.info("Binding services complete...");
    }
}
