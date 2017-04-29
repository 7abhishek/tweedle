/**
 * 
 */
package services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.storm.LocalCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitter.hbc.core.Client;

import models.TweedleRequest;
import play.api.cache.CacheApi;
import play.mvc.WebSocket.Out;
import services.ControlService;
import util.TweedleHelper;

@Singleton
public class ControlServiceImpl implements ControlService{
    
    Logger logger = LoggerFactory.getLogger(ControlServiceImpl.class);
    Producer<String, Object> producer;    
    Client client;
    @Inject
    TweedleHelper helper;
    Map<String,  Producer<String, Object>> producerHolder = new HashMap<String, Producer<String,Object>>();
    Map<String,  Client> clientHolder = new HashMap<>();
    Map<String,  KafkaConsumer<String, Object>> consumerHolder = new HashMap<>(); 
    Map<String ,Out<String>> websocketHolder = new HashMap<>();
    Map<String ,LocalCluster> stormClusterHolder = new HashMap<>();
    
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
        logger.info("Stopping HBC client");
        this.clientHolder.get(helper.getTopicName(tweetRequest)).stop();
        logger.info("Stopping kafka producer");
        this.producerHolder.get(helper.getTopicName(tweetRequest)).close();
//        logger.info("Stopping kafka consumer");
//       this.consumerHolder.get(helper.getTopicName(tweetRequest)).close();
//        logger.info("Stopping websocket connection");
        //this.websocketHolder.get(helper.getTopicName(tweetRequest)).close();
        logger.info("Stopping Storm topology");
        this.stormClusterHolder.get(helper.getTopicName(tweetRequest)).shutdown();
        //removing the instances after closing.        
        this.clientHolder.remove(helper.getTopicName(tweetRequest));
        this.producerHolder.remove(helper.getTopicName(tweetRequest));
        this.consumerHolder.remove(helper.getTopicName(tweetRequest));
       // this.websocketHolder.remove(helper.getTopicName(tweetRequest));
        this.stormClusterHolder.remove(helper.getTopicName(tweetRequest));
        } catch(Exception e){
            logger.error("Exception occurred during stoping tweedle for : {} : {}", tweetRequest ,e.getMessage(),e);
        }
    }
    
    @Override
    public void saveConsumer(TweedleRequest tweetRequest, KafkaConsumer<String, Object> consumer, Out<String> out){
        this.consumerHolder.put(helper.getTopicName(tweetRequest),consumer);
        this.websocketHolder.put(helper.getTopicName(tweetRequest), out);
    }
    
    @Override
    public void saveStormCluster(TweedleRequest tweetRequest, LocalCluster cluster) {
        this.stormClusterHolder.put(helper.getTopicName(tweetRequest), cluster);        
    }

}
