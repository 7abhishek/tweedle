/**
 * 
 */
package producers;

import kafka.consumer.KafkaStream;


public interface KProducer {
        public void SendMessage(String key, Object message, String topic);
        public void close();       
}
