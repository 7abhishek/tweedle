/**
 * 
 */
package topologies;

import java.util.UUID;

import models.TweedleRequest;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.KillOptions;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.Play;
import services.impl.SentimentAnalyzerServiceImpl;
import util.TweedleHelper;
import bolts.AggregatorBolt;
import bolts.SimpleBolt;

/**
 * @author abhishek
 *
 */
public class SimpleTopology implements SimpleTopologyI {

     private Logger logger = LoggerFactory.getLogger(SimpleTopology.class);    
     private String connectionString = Play.application().configuration().getString("zookeeper.server.connection");
     @Inject TweedleHelper tweedleHelper;

    public Boolean startTopology(TweedleRequest tweedleRequest) {
        try {
        Config config = new Config();
        config.setDebug(true);
        config.setNumWorkers(1);
        config.setMaxTaskParallelism(10);       
        config.setDebug(true);
        config.setMaxSpoutPending(5000);
        config.setMessageTimeoutSecs(60);
        config.registerSerialization(SentimentAnalyzerServiceImpl.class);        
        logger.info("zookeeper connectionString : {} ", connectionString);
        String zkConnString = connectionString;
        String topic = tweedleHelper.getTopicName(tweedleRequest);
        BrokerHosts hosts = new ZkHosts(zkConnString);
        logger.info("Initialized Brokerhosts , topic : {} ", topic);        
        SpoutConfig kafkaSpoutConfig = new SpoutConfig(hosts, topic, "/"+topic, UUID.randomUUID().toString());
        kafkaSpoutConfig.bufferSizeBytes = 1024 * 1024 * 4;
        kafkaSpoutConfig.fetchSizeBytes = 1024 * 1024 * 4;
        kafkaSpoutConfig.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();
        kafkaSpoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        logger.info("setting Scheme for kafkaSpoutConfig : {} ", kafkaSpoutConfig);
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-spout", new KafkaSpout(kafkaSpoutConfig), 1);
        logger.info("TopologyBuilder setSpout : KafkaSpout");
        builder.setBolt("simple-bolt", new SimpleBolt()).shuffleGrouping("kafka-spout");
        builder.setBolt("aggregator-bolt", new AggregatorBolt(tweedleRequest.getUserId(),tweedleRequest.getTweedle())).shuffleGrouping("simple-bolt");       
        logger.info("TopologyBuilder setBolt : SimpleBolt");
        LocalCluster cluster = new LocalCluster();
        logger.info("Submitting topology SimpleTopology");
        cluster.submitTopology("SimpleTopology", config, builder.createTopology());
//        Thread.sleep(120000);
//        logger.info("shutting down topology SimpleTopology");
//        cluster.shutdown();
        return true;
        } catch (Exception e){
            logger.error("Exception Occurred  : {} ", e.getMessage(), e);
            return false;
        }
    }
}
