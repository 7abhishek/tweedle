/**
 * 
 */
package dao;

import java.util.List;

import models.TweedleRequest;


public interface TweedleRequestDao {
    public TweedleRequest saveRequest(TweedleRequest tweedleRequest);
    public List<TweedleRequest> getRequestsByUserId(String userId);
    public TweedleRequest getRequestByUserIdAndTweedle(String userId,String tweedle);
}
