/**
 * 
 */
package producers;

import kafka.consumer.KafkaStream;

/**
 * @author abhishek
 *
 */
public interface KProducer {
        public void SendMessage(String key, Object message, String topic);
        public void close();       
}
