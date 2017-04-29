package controllers;

import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.*;
import producers.KafkaProducerImpl;
import producers.Kafkaproducer;
import services.ControlService;
import services.KafkaStreamsService;
import services.Notifier;
import services.SentimentAnalyzerService;
import topologies.SimpleTopology;
import topologies.SimpleTopologyI;
import views.html.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import models.TweedleRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actors.MyWebSocketActor;
import akka.actor.ActorRef;
import akka.pattern.Patterns;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import dao.TweedleRequestDao;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message. The
     * configuration in the <code>routes</code> file means that this method will
     * be called when the application receives a <code>GET</code> request with a
     * path of <code>/</code>.
     */

    Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Inject
    Kafkaproducer producer;
    @Inject
    SimpleTopologyI simpleTopology;

    @Inject
    SentimentAnalyzerService sentimentService;
    @Inject
    KafkaStreamsService kafkaStreamsService;
    @Inject
    Notifier notifier;    

    @Inject
    TweedleRequestDao tweedleRequestDao;
    
    @Inject
    ControlService controlService;
    
    public Promise<Result> health() {
        try {            
            return Promise.promise(() -> ok("Tweedle Service is  Healthy"));
        } catch (Exception e) {
            return Promise.promise(() -> internalServerError("Exception Occurred , Please contact System administrator"));
        }
    }

    /*
     *  return 200 OK for any OPTIONS call.
     */
    public Result options(String path) {
        return ok();
    }

    public Promise<Result> start() {
        try {
            JsonNode requestJson = request().body().asJson();
            //asJson - json node form of the request 
            //request() = method in the framework
            //request is of type GET or POST or any http requests 
            //read request content and fetch only the body part
            TweedleRequest tweedleRequest = Json.fromJson(requestJson, TweedleRequest.class);
            //the json body is converted to tweedleRequest model = this is done to ensure correctness of user
            //supplied data and also its better to convert data to required model and then passed around 
            //to different components of a service using models.Improvisation we can add validators 
            //annotations to model to enforce validation such as not null , not empty on fields 
            //refer javaX bean validation
            tweedleRequestDao.saveRequest(tweedleRequest);
            //db operations are present in daoImpl class
            Promise.promise(() -> producer.activate(tweedleRequest));           
            //gets executed asynchronously in a non blocking        
            return Promise.promise(() -> ok("Started!!"));
        } catch (Exception e) {
            logger.error("Exception at start : {} ", e.getMessage(), e);
            return Promise.promise(() -> internalServerError("Exception Occurred , Please contact System administrator"));
        }

    }

    /*
     *  Test Method to test Sentiment of a random String
     */
    public Result sentiment() {
        try {
            logger.info("HomeController sentiment : {} ", sentimentService.analyze("hello, how are you ?, the sun shines bright today."));
            return ok("sentiment completed!!");
        } catch (Exception e) {
            logger.error("Exception at start : {} ", e.getMessage(), e);
            return internalServerError("Some shit happened!!" + e.getMessage());
        }
    }

    public WebSocket<String> testSocket() {
        logger.info("testSocket Socket established");
        return WebSocket.whenReady((in, out) -> {
            in.onMessage(message -> {
                try{
                    logger.info("message : {} ", message);
                    JsonNode json = Json.parse(message);
                    TweedleRequest request = Json.fromJson(json,TweedleRequest.class);
                    logger.info("request : {} ", request);
                    kafkaStreamsService.stream(request, out);
                } catch(Exception exception) {
                    logger.error("Exception Occurred during streaming Sentiments : {}", exception.getMessage(), exception);
                }
            });
            in.onClose(() -> logger.info("Disconnected"));            
        });
    }
    
    public Promise<Result> stopTweedle() {        
        JsonNode requestJson = request().body().asJson();
        TweedleRequest tweedleRequest = Json.fromJson(requestJson, TweedleRequest.class);
        controlService.stopProducerAndClient(tweedleRequest);
        return Promise.promise(() -> ok());
    }
}
