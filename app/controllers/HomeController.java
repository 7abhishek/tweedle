package controllers;

import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.*;
import producers.KafkaProducerImpl;
import producers.Kafkaproducer;
import services.KafkaStreamsService;
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
    // @Inject
    // @Named("wsActor")
    // ActorRef wsActor;

    @Inject
    TweedleRequestDao tweedleRequestDao;
    public Result index() {
        try {
            return ok("Tweedle is getting started!!! hifi " + InetAddress.getLocalHost().getCanonicalHostName());
        } catch (UnknownHostException e) {
            return internalServerError("Some shit happened!!" + e.getMessage());
        }
    }

    public Result options(String path) {
        return ok();
    }

    public Result start() {
        try {
            JsonNode requestJson = request().body().asJson();
            TweedleRequest tweedleRequest = Json.fromJson(requestJson, TweedleRequest.class);
            tweedleRequestDao.saveRequest(tweedleRequest);
            Promise.promise(() -> producer.activate(tweedleRequest));
            Promise.promise(() -> simpleTopology.startTopology(tweedleRequest));
            return ok("Started!!");
        } catch (Exception e) {
            logger.error("Exception at start : {} ", e.getMessage(), e);
            return internalServerError("Some shit happened!!" + e.getMessage());
        }

    }

    public Result sentiment() {
        try {
            logger.info("HomeController sentiment : {} ", sentimentService.analyze("hello, how are you ?, the sun shines bright today."));
            return ok("sentiment completed!!");
        } catch (Exception e) {
            logger.error("Exception at start : {} ", e.getMessage(), e);
            return internalServerError("Some shit happened!!" + e.getMessage());
        }
    }

    public Result startStream() {
        JsonNode requestJson = request().body().asJson();
        TweedleRequest tweedleRequest = Json.fromJson(requestJson, TweedleRequest.class);
        kafkaStreamsService.stream(tweedleRequest);
        // String resultStream =
        // kafkaStreamsService.stream(tweedleRequest).map((k,v) ->
        // v.toString());
        return ok();
    }

    public WebSocket<String> socketTest() {
        return WebSocket.withActor(MyWebSocketActor::props);
    }

    public WebSocket<String> testSocket() {
        return WebSocket.whenReady((in, out) -> {
            in.onMessage(message -> {
                JsonNode json = Json.parse(message);
                TweedleRequest request = Json.fromJson(json,TweedleRequest.class);
                logger.info("request : {} ", request);
                kafkaStreamsService.stream(request);
                out.write("Hello! " + message);
            });
            in.onClose(() -> logger.info("Disconnected"));
            out.write("Hello!");
        });
    }
}
