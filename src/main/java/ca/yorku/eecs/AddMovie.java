	package ca.yorku.eecs;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AddMovie implements HttpHandler {
	
	

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
        	System.out.println("json error/Already exists in DB");
        	rq.sendResponseHeaders(400, -1);
		}
		
	}


	
	public void handlePut(HttpExchange rq) throws IOException, JSONException{
		
		
		
		String rqBody = Utils.convert(rq.getRequestBody());
		
		
			JSONObject deserialized = new JSONObject(rqBody);
			String name ="";
			String movieID = "";
			double imdbRatings;
			int runtime;
			
			
			if(deserialized.has("movieId") ) {
				
				
				
				movieID = deserialized.getString("movieId");
				if(deserialized.has("name")) {
				name = deserialized.getString("name");} else name = null;
				
				if(deserialized.has("imdbRatings")) {
				imdbRatings = deserialized.getDouble("imdbRatings");} else imdbRatings = 0;
				
				if(deserialized.has("runtime")) {
				runtime = deserialized.getInt("runtime");} else runtime = 0;
				
				Neo4jConnection njc = new Neo4jConnection();
				njc.addMovie(name, movieID, imdbRatings, runtime);
				rq.sendResponseHeaders(200, -1);
				njc.close();
				
			} else {
				
				throw new JSONException("Missing attributes");
			}
			
			
			
		
	}
	
	
	
}
