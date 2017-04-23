/**
 * 
 */
package bolts;

import java.util.HashMap;
import java.util.Map;

import models.Sentiment;
import models.TweedleRequest;
import models.TweetSentiment;
import play.libs.Json;

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
import dao.TweedleSentimentDao;

public class AggregatorBolt implements IRichBolt {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<String, Long> sentimentCount;
    private Sentiment sentimentModel;
    private OutputCollector _collector;
    @Inject
    static TweedleRequestDao tweedleRequestDao;
    @Inject
    static TweedleSentimentDao tweedleSentimentDao;
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
            if(input>0) {
                sentimentModel.incrementPositive();
            }
            if(input==0) {
                sentimentModel.incrementNeutral();
            }
            if(input<0) {
                sentimentModel.incrementNegative();
            }            
            count = count + 1;
            TweedleRequest tweedleRequest = tweedleRequestDao.getRequestByUserIdAndTweedle(userId, tweedle);
            TweetSentiment sentiment = new TweetSentiment();
            sentiment.setRequest(tweedleRequest);
            sentiment.setSentiment(sentimentModel);
            logger.info("saving TweedleSentiment : {} ", Json.toJson(sentiment));
            tweedleSentimentDao.saveTweedleSentiment(sentiment);
            logger.info("sending sentiment stats to kafka topic: {} count : {}", helper.getTopicNameForRepubishing(tweedleRequest),
                    sentimentModel);
            kProducer.SendMessage(count.toString(), sentimentModel, helper.getTopicNameForRepubishing(tweedleRequest));
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
        TweedleRequest tweedleRequest = tweedleRequestDao.getRequestByUserIdAndTweedle(userId, tweedle);
        TweetSentiment sentiment =  tweedleSentimentDao.getTweedleSentiment(tweedleRequest);
        if(sentiment!=null){
            sentimentModel = sentiment.getSentiment();
        } else {
            sentimentModel = new Sentiment();
        }
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
