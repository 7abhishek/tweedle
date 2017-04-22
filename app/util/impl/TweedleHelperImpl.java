/**
 * 
 */
package util.impl;

import models.TweedleRequest;
import util.TweedleHelper;

/**
 * @author abhishek
 *
 */
public class TweedleHelperImpl implements TweedleHelper {

    /*
     * (non-Javadoc)
     * 
     * @see util.TweedleHelper#getTopicName(java.lang.String, java.lang.String)
     */
    @Override
    //first topic will have just raw tweets coming from the streaming api
    public String getTopicName(TweedleRequest tweedleRequest) {
        return tweedleRequest.getUserId() + "-" + tweedleRequest.getTweedle();
    }

    @Override
    //these tweets returned are along with sentiments
    public String getTopicNameForRepubishing(TweedleRequest tweedleRequest) {
        return tweedleRequest.getTweedle() + "-" + tweedleRequest.getUserId();
    }

}
