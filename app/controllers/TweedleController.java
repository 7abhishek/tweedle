/**
 * 
 */
package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dao.TweedleRequestDao;
import models.TweedleRequest;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class TweedleController extends Controller {

    @Inject TweedleRequestDao dao;
    public Promise<Result> getTweedles(String userId) {
        return Promise.promise(() -> ok(Json.toJson(dao.getRequestsByUserId(userId))));
    }
    
    public Promise<Result> saveTweedle() {
        JsonNode json = request().body().asJson(); 
        TweedleRequest request = Json.fromJson(json,TweedleRequest.class);
        return Promise.promise(() -> ok(Json.toJson(dao.saveRequest(request))));
    }

}
