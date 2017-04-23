/**
 * 
 */
package services;

import models.TweedleRequest;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.KStream;

import play.mvc.WebSocket.Out;

/**
 * @author abhishek
 *
 */
public interface KafkaStreamsService {
    public void stream(TweedleRequest tweedleRequest, Out<String> out);
}
