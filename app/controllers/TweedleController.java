/**
 * 
 */
package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dao.TweedleRequestDao;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author abhishek
 *
 */
public class TweedleController extends Controller {

    @Inject TweedleRequestDao dao;
    public Promise<Result> getTweedles(String userId) {
        return Promise.promise(() -> ok(Json.toJson(dao.getRequestsByUserId(userId))));
    }
    
    public Promise<Result> savetTweedle() {
        JsonNode json = request().body().asJson(); 
        return Promise.promise(() -> ok(Json.toJson(dao.)));
    }

}
