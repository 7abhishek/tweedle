/**
 * 
 */
package controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    Logger logger = LoggerFactory.getLogger(TweedleController.class);
    public Promise<Result> getTweedles(String userId) {
        return Promise.promise(() -> ok(Json.toJson(dao.getRequestsByUserId(userId))));
    }
    
    public Promise<Result> saveTweedle() {
        JsonNode json = request().body().asJson(); 
        TweedleRequest request = Json.fromJson(json,TweedleRequest.class);
        logger.info("parsed TweedleRequest  : {} ", Json.toJson(request));
        return Promise.promise(() -> ok(Json.toJson(dao.saveRequest(request))));
    }

}
