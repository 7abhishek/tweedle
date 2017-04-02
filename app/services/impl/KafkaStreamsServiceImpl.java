package services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import models.TweedleRequest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
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
import play.mvc.WebSocket.Out;
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
    public  void stream(TweedleRequest tweedleRequest, Out<String> out) {
        try {
        logger.info("tweedleRequest : {} , bootstrapServers :{}", Json.toJson(tweedleRequest), bootstrapServers);
        String topic = tweedleHelper.getTopicNameForRepubishing(tweedleRequest);
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "fetch-response-starvation");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, Long.toString(Integer.MAX_VALUE));
        props.setProperty(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "40000");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); 
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); 
        KafkaConsumer<String, Object> consumer = new KafkaConsumer<String, Object>(props);
        consumer.subscribe(Arrays.asList(topic));
        logger.info("Subscribed to topic : {} ,  {}" , topic, consumer);       
        while (true) {
            logger.info("while true.......");
           ConsumerRecords<String, Object> records = consumer.poll(Long.MAX_VALUE);
           //consumer.commitAsync();
           logger.info("ConsumerRecords records : {} ", records);                            
              for (ConsumerRecord<String, Object> record : records) {                
                 logger.info(" record key : {} , record value: {} , record offset : {} ", record.key(), record.value(), record.offset());
                 out.write(record.value().toString());
              }
        }            
        } catch (Exception e){
            logger.error("Exception during stream : {}  ", e.getMessage(), e);           
        }
    }

    
}
