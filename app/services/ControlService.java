/**
 * 
 */
package services;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;

import com.twitter.hbc.core.Client;

import models.TweedleRequest;
import play.mvc.WebSocket.Out;

public interface ControlService {
          public void saveKafkaProducer(TweedleRequest tweetRequest,Producer<String, Object> producer);
          public void saveHbcClient(TweedleRequest tweetRequest, Client client);
          public void saveConsumer(TweedleRequest tweetRequest, KafkaConsumer<String, Object> consumer, Out<String> out);
          public void stopProducerAndClient(TweedleRequest tweetRequest);
}
