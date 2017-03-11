/**
 * 
 */
package producers;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.storm.shade.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import services.Notifier;

import com.google.inject.Inject;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

/**
 * @author abhishek
 *
 */
public class KafkaProducerImpl implements Kafkaproducer {
    
    Notifier notifier;
    Configuration conf;
    final String topic = "tweedle-topic";
    Logger logger = LoggerFactory.getLogger(KafkaProducerImpl.class);
    @Inject
    public KafkaProducerImpl(Notifier notifier, Configuration conf){
        this.notifier = notifier;
        this.conf = conf;
    }
    public Boolean activate() {
        try {
            String consumerKey= conf.getString("twitter.consumerKey");
            String consumerSecret= conf.getString("twitter.consumerSecret");
            String token=conf.getString("twitter.token");
            String secret=conf.getString("twitter.secret");
            logger.info("activate with consumerKey :{} , consumerSecret {} , token {} , secret {}", consumerKey, consumerSecret, token,
                    secret, "");
            Properties properties = new Properties();
            Integer retries = 0;
            properties.put("bootstrap.servers", "localhost:9092");
            properties.put("acks", "all");
            properties.put("retries", retries);
            properties.put("batch.size", 16384);
            properties.put("linger.ms", 1);
            properties.put("buffer.memory", 33554432);
            properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");            
            Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);
            logger.info("setting  properties...");
            BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
            StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
            // add some track terms
            endpoint.trackTerms(Lists.newArrayList("abhishek"));
            logger.info("adding trackTerms");
            Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
            Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
                    .processor(new StringDelimitedProcessor(queue)).build();

            // Establish a connection
            client.connect();
            logger.info("client trying to connect...");
            // Do whatever needs to be done with messages
            for (int msgRead = 0; msgRead < 1000; msgRead++) {
                ProducerRecord<String, String> message = null;
                String strMsg = queue.take();
                logger.info("index : {} , strMsg : {} ", msgRead, strMsg);
                message = new ProducerRecord<String, String>(topic, Integer.toString(msgRead), strMsg);
                producer.send(message);
                logger.info("sending notification now..");                
                notifier.sendMessage(strMsg);
            }
            producer.close();
            client.stop();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
