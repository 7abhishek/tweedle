/**
 * 
 */
package bolts;

import java.util.HashMap;
import java.util.Map;

import models.TweedleRequest;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import producers.KProducer;
import services.SentimentAnalyzerService;
import util.TweedleHelper;

import com.google.inject.Inject;

import dao.TweedleRequestDao;

/**
 * @author abhishek
 *
 */
public class AggregatorBolt implements IRichBolt {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<String, Long> sentimentCount;
    private OutputCollector _collector;
    @Inject
    static TweedleRequestDao tweedleRequestDao;
    private String userId;
    private String tweedle;
    private Integer count = 0;
    @Inject
    static TweedleHelper helper;
    @Inject
    static KProducer kProducer;
    static final Logger logger = LoggerFactory.getLogger(AggregatorBolt.class);
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.storm.task.IBolt#cleanup()
     */

    public AggregatorBolt(String userId, String tweedle) {
        this.userId = userId;
        this.tweedle = tweedle;
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.storm.task.IBolt#execute(org.apache.storm.tuple.Tuple)
     */
    @Override
    public void execute(Tuple arg0) {
        try {
            Long input = arg0.getLong(0);
            String sentiment = input > 0 ? "positive" : ((input < 0) ? "negative" : "neutral");
            Long newCount = sentimentCount.get(sentiment) + 1;
            sentimentCount.put(sentiment, newCount);
            count = count + 1;
            TweedleRequest tweedleRequest = tweedleRequestDao.getRequestByUserIdAndTweedle(userId, tweedle);
            logger.info("sending sentiment stats to kafka topic: {} count : {}", helper.getTopicNameForRepubishing(tweedleRequest),
                    sentimentCount.toString());
            kProducer.SendMessage(count.toString(), sentimentCount, helper.getTopicNameForRepubishing(tweedleRequest));
            _collector.ack(arg0);
        } catch (Exception e) {
            logger.error("Exception occurred while aggregating sentiment : {} ", e.getMessage(), e);
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
        // TODO Auto-generated method stub
        sentimentCount = new HashMap<String, Long>();
        sentimentCount.put("neutral", 0L);
        sentimentCount.put("positive", 0L);
        sentimentCount.put("negative", 0L);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.storm.topology.IComponent#declareOutputFields(org.apache.storm
     * .topology.OutputFieldsDeclarer)
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        // TODO Auto-generated method stub
        // arg0.declare(new Fields("SentimentCount"));
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
