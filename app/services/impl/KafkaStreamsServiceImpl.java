package services.impl;

import java.util.Arrays;
import java.util.Properties;

import models.TweedleRequest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.Play;
import play.libs.Json;
import services.KafkaStreamsService;
import util.TweedleHelper;

/**
 * @author abhishek
 *
 */
public class KafkaStreamsServiceImpl implements KafkaStreamsService{

    Logger logger = LoggerFactory.getLogger(KafkaStreamsServiceImpl.class);
    String bootstrapServers = Play.application().configuration().getString("kafka.server.bootstrap.servers.string");
    String zooperKeeper =  Play.application().configuration().getString("zookeeper.server.connection");    
    @Inject TweedleHelper tweedleHelper;
   
   
    /* (non-Javadoc)
     * @see services.KafkaStreamsService#stream()
     */
    @Override
    public  void stream(TweedleRequest tweedleRequest) {
        try {
        logger.info("tweedleRequest : {} ", Json.toJson(tweedleRequest));        
//        Properties streamsConfiguration = new Properties();
//        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
//        // against which the application is run.
//        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-tweedle-example");
//        // Where to find Kafka broker(s).
//        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        // Where to find the corresponding ZooKeeper ensemble.
//        streamsConfiguration.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, zooperKeeper);
//        // Specify default (de)serializers for record keys and for record values.
//        streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
//        streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
//        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");  
//        KStreamBuilder builder = new KStreamBuilder();
//        // We assume the input topic contains records where the values are Integers.
//        // We don't really care about the keys of the input records;  for simplicity, we assume them
//        // to be Integers, too, because we will re-key the stream later on, and the new key will be
//        // of type Integer.
//        logger.info("topic : {}", tweedleHelper.getTopicNameForRepubishing(tweedleRequest));
//        KStream<String, Object> kStream = builder.stream(tweedleHelper.getTopicNameForRepubishing(tweedleRequest));    
//        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);       
//        streams.start();
//       
//        return kStream;
        String topic = tweedleHelper.getTopicNameForRepubishing(tweedleRequest);
        String group = tweedleHelper.getTopicNameForRepubishing(tweedleRequest);
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", group);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        
        consumer.subscribe(Arrays.asList(topic));
        logger.info("Subscribed to topic : {} ,  {}" , topic, consumer);        
           
        while (true) {
           ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
           logger.info("ConsumerRecords records : {} ", records);
           consumer.commitSync();           
              for (ConsumerRecord<String, String> record : records) {                
                 logger.info(" record : {} , value: {} ",record, record.value());
              }
        }            
        } catch (Exception e){
            logger.error("Exception during stream : {}  ", e.getMessage(), e);           
        }
    }

    
}
