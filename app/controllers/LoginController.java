/**
 * 
 */
package controllers;

import models.TweedleUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dao.UserDao;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class LoginController extends Controller{

    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Inject UserDao userDao;
    
    public Result login() {
        JsonNode requestJson = request().body().asJson();        
        TweedleUser user = Json.fromJson(requestJson, TweedleUser.class);
        if(session(user.getUserId())!=null){
            logger.info("user session already present : {} ", session(user.getUserId()));
        } else {
            logger.info("setting new session for user Id : {} ", user.getUserId());
            session(user.getUserId(),user.getName());
        }
        return ok(Json.toJson(userDao.addUser(user)));        
    }
    
}
