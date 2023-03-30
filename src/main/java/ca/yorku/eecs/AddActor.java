package ca.yorku.eecs;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AddActor implements HttpHandler {
	
	

	@Override
	public void handle(HttpExchange rq) throws IOException {
		// TODO Auto-generated method stub
		
		try {
            if (rq.getRequestMethod().equals("PUT")) {
                handlePut(rq);
               
            } else rq.sendResponseHeaders(400, -1);
        } catch (IOException e) {
        	rq.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
			// TODO Auto-generated catch block
        	rq.sendResponseHeaders(400, -1);
		} catch (a404Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
	public void handlePut(HttpExchange rq) throws IOException, JSONException, a404Exception{
		
		
		
		String rqBody = Utils.convert(rq.getRequestBody());
		
		
			JSONObject deserialized = new JSONObject(rqBody);
			String name ="";
			String actorID = "";
			int age;
			
			System.out.println("Point 1");
			
			if(deserialized.has("actorId")) {
				if(deserialized.has("name")) {
				name = deserialized.getString("name");}
				else name = "";
				
				actorID = deserialized.getString("actorId");
				
				if(deserialized.has("age")) {
					age = deserialized.getInt("age");}
					else age = 0;
				
				
				System.out.println("Point 2");
				Neo4jConnection njc = new Neo4jConnection();
				njc.addActor(name, actorID, age);
				rq.sendResponseHeaders(200, -1);
				njc.close();
			}
			
			else throw new JSONException("Missing some attributes");
			
			
			
		
	}
	
	
	
}
