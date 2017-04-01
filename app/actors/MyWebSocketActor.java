package actors;

import models.TweedleRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import play.libs.Json;
import services.KafkaStreamsService;
import akka.actor.*;

public class MyWebSocketActor extends UntypedActor {

    Logger logger = LoggerFactory.getLogger(MyWebSocketActor.class);
    @Inject KafkaStreamsService kafkaStreamsService;
    public static Props props(ActorRef out) {
        return Props.create(MyWebSocketActor.class, out);
    }

    private final ActorRef out;

    public MyWebSocketActor(ActorRef out) {
        this.out = out;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            JsonNode json = Json.parse(message.toString());
            TweedleRequest request = Json.fromJson(json, TweedleRequest.class);
            logger.info("actor was invoked with string : {} ", message);
            out.tell("I received your message: " + message, self());
            out.tell("I received your message: " + message, self());
            out.tell("I received your message: " + message, self());
            out.tell("I received your message: " + message, self());
            out.tell("I received your message: " + message, self());           
        }
    }
}