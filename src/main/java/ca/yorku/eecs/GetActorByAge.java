package ca.yorku.eecs;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GetActorByAge implements HttpHandler {

	@Override
	public void handle(HttpExchange rq) throws IOException {
		// TODO Auto-generated method stub
		
		try {
            if (rq.getRequestMethod().equals("GET")) {
                handleGet(rq);
               
            } else rq.sendResponseHeaders(400, -1);
        } catch (IOException e) {
        	rq.sendResponseHeaders(500, -1);
        }
		
	}


	
	public void handleGet(HttpExchange rq) throws IOException{
		
		
		
		String rqBody = Utils.convert(rq.getRequestBody());
		
		try {
			JSONObject deserialized = new JSONObject(rqBody);
			
			int actorAge ;
			
			
			if(deserialized.has("age")) {
				
				actorAge = deserialized.getInt("age");
				
				Neo4jConnection njc = new Neo4jConnection();
				String data = njc.getActorByAge(actorAge);
				njc.close();
				
				this.sendString(rq, data, 200);
			}
			
			else rq.sendResponseHeaders(400, -1);
			
			
			
		} catch(Exception e ) {
			rq.sendResponseHeaders(500, -1);
		} 
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
