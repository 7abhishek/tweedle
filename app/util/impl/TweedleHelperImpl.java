/**
 * 
 */
package util.impl;

import models.TweedleRequest;
import util.TweedleHelper;

public class TweedleHelperImpl implements TweedleHelper {
  
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
