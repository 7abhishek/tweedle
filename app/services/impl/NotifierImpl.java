/**
 * 
 */
package services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import play.Configuration;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import services.Notifier;

public class NotifierImpl implements Notifier {
    
    WSClient ws;
    Configuration conf;
    public static final String PUSHWOOSH_SERVICE_BASE_URL = "https://cp.pushwoosh.com/json/1.3/";
    private static final String AUTH_TOKEN = "mftgIIgS1pOTsNGZgaJ03M5D0mV1Tm6eBjSFb1yyAtaHsLjAqtZwdDIdzErLqhxPJJFdI9EokUbFNNv2sOxi";
    private static final String APPLICATION_CODE = "8A59A-4317A";
 
    Logger logger = LoggerFactory.getLogger(NotifierImpl.class);
    @Inject
    public NotifierImpl(WSClient ws, Configuration conf){
        this.ws = ws;
        this.conf = conf;
    }
    
    @Override
    public void sendPushNotification(String message){
        try{
        String method = "createMessage";
        String url = PUSHWOOSH_SERVICE_BASE_URL + method;
        ObjectNode result = Json.newObject();
        result.put("send_date", "now");
        result.put("content", message);
        result.put("link", "http://pushwoosh.com/");
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        array.add(result);
 
        ObjectNode requestObject = Json.newObject();
        requestObject.put("application", APPLICATION_CODE);
        requestObject.put("auth", AUTH_TOKEN);
        requestObject.put("notifications", array);
        requestObject.put("android_priority",2); 
        ObjectNode mainRequest = Json.newObject();
        mainRequest.put("request", requestObject);
        Promise<WSResponse> response = ws.url(url).setHeader("Content-Type", "application/json").post(mainRequest);
        response.map(results -> {
            logger.info("sendMessage2 response : {} ",  results);
            return results;
        });
        } catch (Exception e){
            logger.error("Exception Occurred at sending push notification : {} ", e.getMessage(), e);
        }
    }

}
