/**
 * 
 */
package topologies;

import java.util.UUID;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bolts.SimpleBolt;

/**
 * @author abhishek
 *
 */
public class SimpleTopology {

    static Logger logger = LoggerFactory.getLogger(SimpleTopology.class);
    public static void main(String[] args) throws Exception{
        Config config = new Config();
        config.setDebug(true);
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        String zkConnString = "localhost:2181";
        String topic = "tweedle-topic";
        BrokerHosts hosts = new ZkHosts(zkConnString);
        
        SpoutConfig kafkaSpoutConfig = new SpoutConfig (hosts, topic, "/" + topic,    
           UUID.randomUUID().toString());
        kafkaSpoutConfig.bufferSizeBytes = 1024 * 1024 * 4;
        kafkaSpoutConfig.fetchSizeBytes = 1024 * 1024 * 4;
        //kafkaSpoutConfig.forceFromStart = true;
        kafkaSpoutConfig.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();
        kafkaSpoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-spout", new KafkaSpout(kafkaSpoutConfig));
        builder.setBolt("simple bolt", new  SimpleBolt()).shuffleGrouping("kafka-spout");          
        LocalCluster cluster = new LocalCluster();
        logger.info("Submitting topology KafkaStormSample");
        cluster.submitTopology("KafkaStormSample", config, builder.createTopology());
//        Thread.sleep(10000);
        
//        cluster.shutdown();
     }
}
