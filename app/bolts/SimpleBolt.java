/**
 * 
 */
package bolts;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @author abhishek
 *
 */
public class SimpleBolt implements IRichBolt {
    private OutputCollector collector;
    
    @Override
    public void cleanup() {

    }

    @Override
    public void execute(Tuple input) {
        String tweet = input.getString(0);
        String[] words = tweet.split(",");        
        collector.emit(new Values(words.toString()));
        collector.ack(input);
    }

    @Override
    public void prepare(Map arg0, TopologyContext arg1, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweet"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}
