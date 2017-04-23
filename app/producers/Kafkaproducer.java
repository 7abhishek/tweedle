/**
 * 
 */
package producers;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.Producer;

import com.twitter.hbc.core.Client;

import models.TweedleRequest;


public interface Kafkaproducer {
    public Boolean activate(TweedleRequest tweedleRequest);
}
