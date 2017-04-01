/**
 * 
 */
package services;

import models.TweedleRequest;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.KStream;

/**
 * @author abhishek
 *
 */
public interface KafkaStreamsService {
    public void stream(TweedleRequest tweedleRequest);
}
