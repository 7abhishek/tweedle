/**
 * 
 */
package producers;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.Configuration;
import play.libs.Json;


public class KproducerImpl implements KProducer {

    Configuration conf;
    Producer<String, Object> producer;
    Logger logger = LoggerFactory.getLogger(KproducerImpl.class);
    /* (non-Javadoc)
     * @see producers.KProducer#SendMessage(java.lang.String)
     */
    @Inject
    KproducerImpl(Configuration conf){
        this.conf = conf;
        String bootstrapServers = this.conf.getString("kafka.server.bootstrap.servers.string");        
        Properties properties = new Properties();
        Integer retries = 3; // Retry Mechanism, value = 3
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("acks", "all");
        properties.put("retries", retries);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");            
        producer = new org.apache.kafka.clients.producer.KafkaProducer<String, Object>(properties);
    }
    
    @Override
    public void SendMessage(String key, Object message, String topic) {     
        try{
        logger.info("bootstrap servers : {} ", this.conf.getString("kafka.server.bootstrap.servers.string"));
        ProducerRecord<String, Object> sendMessage = new ProducerRecord<String, Object>(topic, key, Json.toJson(message).toString());
        logger.info("index : {} , strMsg : {} ", key, message.toString());
        Future<RecordMetadata> result = producer.send(sendMessage);
        try {
            logger.info("result : {} ", result.get());
        } catch (InterruptedException e) {            
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        } catch (Exception e) {
            logger.error("Exception while SendMessage  :{} ",e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        producer.close();
    }
}
