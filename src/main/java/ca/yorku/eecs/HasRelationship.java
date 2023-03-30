package ca.yorku.eecs;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HasRelationship implements HttpHandler {

	@Override
	public void handle(HttpExchange rq) throws IOException {
		// TODO Auto-generated method stub
		
		try {
            if (rq.getRequestMethod().equals("GET")) {
                handleGet(rq);
               
            } else rq.sendResponseHeaders(400, -1);
        } catch (IOException e) {
        	System.out.println("wtf");
        	rq.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
			// TODO Auto-generated catch block
        	rq.sendResponseHeaders(400, -1);
		} catch (a404Exception e) {
			// TODO Auto-generated catch block
			rq.sendResponseHeaders(404, -1);
		}
		
	}


	
	public void handleGet(HttpExchange rq) throws IOException, JSONException, a404Exception{
		
		
		
		String rqBody = Utils.convert(rq.getRequestBody());
		
		
			JSONObject deserialized = new JSONObject(rqBody);
			
			String movieId = "";
			String actorId = "";
			System.out.println("1");
			
			if(deserialized.has("movieId") && deserialized.has("actorId")) {
				
				movieId = deserialized.getString("movieId");
				actorId = deserialized.getString("actorId");
				System.out.println("2");
				Neo4jConnection njc = new Neo4jConnection();
				String data = njc.hasRelationsihip(movieId, actorId);
				njc.close();
				System.out.println("3");
				this.sendString(rq, data, 200);
			}
			
			else rq.sendResponseHeaders(400, -1);
			
			
		
	}
	
	//Reference - basichttpserver code from eclass
	
	private void sendString(HttpExchange rq, String data, int restCode) 
			throws IOException {
		rq.sendResponseHeaders(restCode, data.length());
        OutputStream os = rq.getResponseBody();
        os.write(data.getBytes());
        os.close();
	}

}
