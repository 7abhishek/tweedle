/**
 * 
 */
package util;

import models.TweedleRequest;

/**
 * @author abhishek
 *
 */
public interface TweedleHelper {
    public String getTopicName(TweedleRequest tweedleRequest);
    public String getTopicNameForRepubishing(TweedleRequest tweedleRequest);
}
