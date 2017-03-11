/**
 * 
 */
package services.impl;

import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import play.Configuration;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import services.Notifier;

/**
 * @author abhishek
 *
 */
public class NotifierImpl implements Notifier {
    
    WSClient ws;
    Configuration conf;
    Logger logger = LoggerFactory.getLogger(NotifierImpl.class);
    @Inject
    public NotifierImpl(WSClient ws, Configuration conf){
        this.ws = ws;
        this.conf = conf;
    }
    
    public void sendMessage(String message){
        JsonNode jsonMessage = Json.parse(message);
        String textMessage = jsonMessage.get("text").asText();
        logger.info("NotifierImpl sendMessage");
        String url = conf.getString("pushover.sendmessage.url");
        JsonNode node = Json.newObject().put("token", conf.getString("pushover.token"))
        .put("user", conf.getString("pushover.user"))
        .put("device", conf.getString("pushover.device")) 
        .put("title", "Tweedle Message")
        .put("message", textMessage);      
        StringBuilder stringb = new StringBuilder(); 
        stringb.append("token="+conf.getString("pushover.token"));
        stringb.append("&user="+conf.getString("pushover.user"));
        stringb.append("&device="+conf.getString("pushover.device"));
        stringb.append("&title=Tweedle Message");
        stringb.append("&message="+textMessage);        
        logger.info("json payload : {} ", stringb.toString());
        CompletionStage<WSResponse> response =  ws.url(url).setContentType("application/x-www-form-urlencoded").post(stringb.toString());
        CompletionStage<JsonNode> jsonResponse = response.thenApply(res -> {
            logger.info("notification Response : {} ", res.asJson());
            return res.asJson();   
        });
    }

}
