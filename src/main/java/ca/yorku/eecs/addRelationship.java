package ca.yorku.eecs;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class addRelationship implements HttpHandler {
	
	


	@Override
	public void handle(HttpExchange rq) throws IOException {
		// TODO Auto-generated method stub
		
		try {
            if (rq.getRequestMethod().equals("PUT")) {
            
                handlePut(rq);
               
            } else rq.sendResponseHeaders(400, -1);
        } catch (IOException e) {
        	rq.sendResponseHeaders(500, -1);
        } catch (a404Exception e2) {
        
        	rq.sendResponseHeaders(404, -1);
        } catch (JSONException e) {
			// TODO Auto-generated catch block
        	rq.sendResponseHeaders(400, -1);
		}
		
	}


	
	public void handlePut(HttpExchange rq) throws IOException, a404Exception, JSONException{
		
		
		
		String rqBody = Utils.convert(rq.getRequestBody());
		
		
			JSONObject deserialized = new JSONObject(rqBody);
			String actorID ="";
			String movieID = "";
		
			if(deserialized.has("actorId") && deserialized.has("movieId")) {
				actorID = deserialized.getString("actorId");
				movieID = deserialized.getString("movieId");
				
				
				Neo4jConnection njc = new Neo4jConnection();
				njc.insertRelation(actorID, movieID);
				rq.sendResponseHeaders(200, -1);
				njc.close();
			}
			
			else {
				
				throw new JSONException("Missing some attributes");
			}
			
			
			
		
	}
	
	
	
}
