package controllers;

import play.mvc.*;
import producers.KafkaProducerImpl;
import topologies.SimpleTopology;
import views.html.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

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
    @Inject  KafkaProducerImpl producer;
    public Result index() {
        try {
            return ok("Library " + InetAddress.getLocalHost().getCanonicalHostName());
        } catch (UnknownHostException e) {            
            return internalServerError("Some shit happened!!" + e.getMessage());
        }
    }

    public CompletionStage<Result> start() {
        try {
            logger.info("Starting kafka producer");      
//            SimpleTopology simpleTopology = new SimpleTopology();
//            String[] args = {};
//            simpleTopology.main(args);
            CompletableFuture<Boolean> result = CompletableFuture.completedFuture(producer.activate());
            return CompletableFuture.completedFuture(ok("Started!!"));
        } catch (Exception e) {
            logger.error("Exception at start : {} ", e.getMessage(),e);
            return CompletableFuture.completedFuture(internalServerError("Some shit happened!!" + e.getMessage()));
        }

    }
}
