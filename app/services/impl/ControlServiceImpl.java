/**
 * 
 */
package services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitter.hbc.core.Client;

import models.TweedleRequest;
import play.api.cache.CacheApi;
import services.ControlService;
import util.TweedleHelper;

/**
 * @author abhishek
 *
 */
@Singleton
public class ControlServiceImpl implements ControlService{
    
    Logger logger = LoggerFactory.getLogger(ControlServiceImpl.class);
    Producer<String, Object> producer;    
    Client client;
    @Inject
    TweedleHelper helper;
    Map<String,  Producer<String, Object>> producerHolder = new HashMap<String, Producer<String,Object>>();
    Map<String,  Client> clientHolder = new HashMap<>();
    @Inject CacheApi cache;
    
    @Override
    public void saveKafkaProducer(TweedleRequest tweetRequest, Producer<String, Object> producer) {    
        logger.info("saveKafkaProducer for tweetRequest :{} ",tweetRequest);
        this.producerHolder.put(helper.getTopicName(tweetRequest), producer);
        logger.info("this.clientHolder :{} ",this.producerHolder);        
    }
    @Override
    public void saveHbcClient(TweedleRequest tweetRequest, Client client) {
        logger.info("saveHbcClient for tweetRequest :{} ",tweetRequest);
        this.clientHolder.put(helper.getTopicName(tweetRequest), client);
        logger.info("this.clientHolder :{} ",this.clientHolder);
    }

    @Override
    public void stopProducerAndClient(TweedleRequest tweetRequest) {
        try {
        logger.info("stopProducerAndClient for tweetRequest :{} , clientHolder : {} , producerHolder :{}", tweetRequest, this.clientHolder, this.producerHolder);
        logger.info("client : {} ",  this.clientHolder.get(helper.getTopicName(tweetRequest)));
        logger.info("producerHolder : {} ",  this.producerHolder.get(helper.getTopicName(tweetRequest)));
        this.clientHolder.get(helper.getTopicName(tweetRequest)).stop();
        this.producerHolder.get(helper.getTopicName(tweetRequest)).close();
        } catch(Exception e){
            logger.error("Exception occurred during stoping tweedle for : {} : {}", tweetRequest ,e.getMessage(),e);
        }
    }

}
