/**
 * 
 */
package producers;

import java.util.concurrent.CompletableFuture;

import models.TweedleRequest;

/**
 * @author abhishek
 *
 */
public interface Kafkaproducer {
    public Boolean activate(TweedleRequest tweedleRequest);
}
