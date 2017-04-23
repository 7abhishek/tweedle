/**
 * 
 */
package util.impl;

import models.TweedleRequest;
import util.TweedleHelper;

public class TweedleHelperImpl implements TweedleHelper {
  
    @Override
    public String getTopicName(TweedleRequest tweedleRequest) {
        return tweedleRequest.getUserId() + "-" + tweedleRequest.getTweedle();
    }

    @Override
    public String getTopicNameForRepubishing(TweedleRequest tweedleRequest) {
        return tweedleRequest.getTweedle() + "-" + tweedleRequest.getUserId();
    }

}
