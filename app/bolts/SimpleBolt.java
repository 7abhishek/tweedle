/**
 * 
 */
package bolts;

import java.io.IOException;
import java.util.Map;

import models.Tweet;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.Json;
import services.SentimentAnalyzerService;
import services.impl.SentimentAnalyzerServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class SimpleBolt implements IRichBolt {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(SimpleBolt.class);
    private OutputCollector _collector;
    @Inject static SentimentAnalyzerService sentimentAnalyzer;
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.storm.task.IBolt#execute(org.apache.storm.tuple.Tuple)
     */
    @Override
    public void execute(Tuple arg0) {
        try {
            String input = arg0.getString(0);
            ObjectMapper mapper = new ObjectMapper();            
            Tweet tweet = Json.fromJson(mapper.readTree(input), Tweet.class);            
            logger.info("tweet text : {} ", tweet.getText());
            Long sentiment = sentimentAnalyzer.analyze(tweet.getText());
            _collector.emit(new Values(sentiment));
            _collector.ack(arg0);
        } catch (Exception e) {
            logger.error("Exception occurred while parsing Tweet : {} ", e.getMessage(), e);
            e.printStackTrace();
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.storm.task.IBolt#prepare(java.util.Map,
     * org.apache.storm.task.TopologyContext,
     * org.apache.storm.task.OutputCollector)
     */
    @Override
    public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
        this._collector = arg2;
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.storm.topology.IComponent#declareOutputFields(org.apache.storm
     * .topology.OutputFieldsDeclarer)
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("Sentiment"));

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.storm.task.IBolt#cleanup()
     */
    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.storm.topology.IComponent#getComponentConfiguration()
     */
    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }
}
