/**
 * 
 */
package di;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.akka.AkkaGuiceSupport;
import producers.KProducer;
import producers.KafkaProducerImpl;
import producers.Kafkaproducer;
import producers.KproducerImpl;
import providers.DefaultDataStoreProvider;
import services.ControlService;
import services.KafkaStreamsService;
import services.Notifier;
import services.SentimentAnalyzerService;
import services.impl.ControlServiceImpl;
import services.impl.KafkaStreamsServiceImpl;
import services.impl.NotifierImpl;
import services.impl.SentimentAnalyzerServiceImpl;
import topologies.SimpleTopology;
import topologies.SimpleTopologyI;
import util.TweedleHelper;
import util.impl.TweedleHelperImpl;
import actors.MyWebSocketActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import bolts.AggregatorBolt;
import bolts.SimpleBolt;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import dao.TweedleRequestDao;
import dao.TweedleSentimentDao;
import dao.TweetDao;
import dao.UserDao;
import dao.impl.TweedleRequestDaoImpl;
import dao.impl.TweedleSentimentDaoImpl;
import dao.impl.TweetDaoImpl;
import dao.impl.UserDaoImpl;
import models.TweetSentiment;
public class TweedleModule extends AbstractModule implements AkkaGuiceSupport {

    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    Logger logger  = LoggerFactory.getLogger(TweedleModule.class);
    
    @Override
    protected void configure() {
        requestStaticInjection(SimpleBolt.class);
        requestStaticInjection(AggregatorBolt.class);
        bind(Notifier.class).to(NotifierImpl.class);
        bind(Kafkaproducer.class).to(KafkaProducerImpl.class);
        bind(Datastore.class).toProvider(DefaultDataStoreProvider.class);
        bind(UserDao.class).to(UserDaoImpl.class);
        bind(SimpleTopologyI.class).to(SimpleTopology.class);
        bind(SentimentAnalyzerService.class).to(SentimentAnalyzerServiceImpl.class);
        bind(TweedleHelper.class).to(TweedleHelperImpl.class);
        bind(KProducer.class).to(KproducerImpl.class);
        bind(KafkaStreamsService.class).to(KafkaStreamsServiceImpl.class);
        bind(TweedleRequestDao.class).to(TweedleRequestDaoImpl.class);
        bind(TweedleSentimentDao.class).to(TweedleSentimentDaoImpl.class);
        bind(ControlService.class).to(ControlServiceImpl.class).asEagerSingleton();
        bind(TweetDao.class).to(TweetDaoImpl.class);
        logger.info("Binding services complete...");
    }
}
