/**
 * 
 */
package services;

import org.apache.kafka.clients.producer.Producer;

import com.twitter.hbc.core.Client;

import models.TweedleRequest;

public interface ControlService {
          public void saveKafkaProducer(TweedleRequest tweetRequest,Producer<String, Object> producer);
          public void saveHbcClient(TweedleRequest tweetRequest, Client client);
          public void stopProducerAndClient(TweedleRequest tweetRequest);
}
