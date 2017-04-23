/**
 * 
 */
package util;

import models.TweedleRequest;

public interface TweedleHelper {
    public String getTopicName(TweedleRequest tweedleRequest);
    public String getTopicNameForRepubishing(TweedleRequest tweedleRequest);
}
