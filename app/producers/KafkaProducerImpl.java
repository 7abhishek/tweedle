/**
 * 
 */
package producers;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import models.TweedleRequest;
import models.Tweet;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.storm.shade.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.libs.F.Promise;
import play.libs.Json;
import services.ControlService;
import services.Notifier;
import topologies.SimpleTopology;
import topologies.SimpleTopologyI;
import util.TweedleHelper;

import com.google.inject.Inject;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import dao.TweetDao;

public class KafkaProducerImpl implements Kafkaproducer {

    Notifier notifier;
    Configuration conf;
    TweedleHelper tweedleHelper;
    SimpleTopologyI simpleTopology;
    ControlService controlService;
    TweetDao tweetDao;

    Logger logger = LoggerFactory.getLogger(KafkaProducerImpl.class);
    @Inject
    public KafkaProducerImpl(Notifier notifier, Configuration conf, TweedleHelper tweedleHelper, KProducer kProducer, SimpleTopologyI simpleTopology, ControlService controlService,TweetDao tweetDao) {
        this.notifier = notifier;
        this.conf = conf;
        this.tweedleHelper = tweedleHelper;
        this.simpleTopology = simpleTopology;
        this.controlService = controlService;
        this.tweetDao = tweetDao;
    }
    public Boolean activate(TweedleRequest tweedleRequest) {
        try {
            final String topic = tweedleHelper.getTopicName(tweedleRequest);
            //coming from util
            //
            String consumerKey = conf.getString("twitter.consumerKey");
            String consumerSecret = conf.getString("twitter.consumerSecret");
            String token = conf.getString("twitter.token");
            String secret = conf.getString("twitter.secret");
            
            BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
            //related to hbc client

            StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
            // add some track terms
            endpoint.trackTerms(Arrays.asList(tweedleRequest.getTrackTerms().split(",")));
            //shows logging messages in the console
            logger.info("adding trackTerms : {} ", tweedleRequest.getTrackTerms());            
            Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
            Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
                    .processor(new StringDelimitedProcessor(queue)).build();

            // Establish a connection
            client.connect();           //hbc is done 
            logger.info("client trying to connect...");

            String bootstrapServers = this.conf.getString("kafka.server.bootstrap.servers.string");        
            
            Properties properties = new Properties();
            Integer retries = 3;
            //default values from kafka documentation
            properties.put("bootstrap.servers", bootstrapServers);
            properties.put("acks", "all");
            properties.put("retries", retries); // Retry Mechanism, value = 3
            properties.put("batch.size", 16384);
            properties.put("linger.ms", 1);
            properties.put("buffer.memory", 33554432);
            properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");            
            Producer<String, Object> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, Object>(properties);
          //now sending messages to the topic
            //properties internally used to connect to the server
            producer.send(new ProducerRecord<String, Object>(topic, "0", ""));//sending blank message to the channel so that that topic gets created in the server if absent
            controlService.saveHbcClient(tweedleRequest, client);
            controlService.saveKafkaProducer(tweedleRequest, producer);

            
            
            //line 111 starts asynchronously in a non blocking way
            

            Promise.promise(() -> simpleTopology.startTopology(tweedleRequest));
            // Do whatever needs to be done with messages
            int msgRead = 0;
            while (!client.isDone()) {
                String strMsg = queue.take();
                //linked blocking queue ; hbc client will be pushing tweets from one end of the blocking q and queue method will reading the tweets from the other end
                //converting tweet ot model before processing;
                Tweet tweet = Json.fromJson(Json.parse(strMsg), Tweet.class);
                tweetDao.saveTweet(tweet);
                logger.info("index : {} , strMsg : {} ", msgRead, strMsg.toString());

                ProducerRecord<String, Object> sendMessage = new ProducerRecord<String, Object>(topic, Integer.toString(msgRead), strMsg);
                logger.info("producer send  :{} ",producer.send(sendMessage));             
               if(tweedleRequest.getNotify()){
                   //model.getText
                   logger.info("sending notification now.. : {} ", tweet.getText());
                   notifier.sendPushNotification(tweet.getText());
               }
               msgRead  = msgRead +1;
            }
            return true;
        } catch (Exception e) {
            logger.error("Exception occurred at activate : {}", e.getMessage(), e);  
            return false;
        }
    }
}
