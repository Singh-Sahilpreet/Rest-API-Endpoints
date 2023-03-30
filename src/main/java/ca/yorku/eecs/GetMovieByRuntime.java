package ca.yorku.eecs;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GetMovieByRuntime implements HttpHandler {

	@Override
	public void handle(HttpExchange rq) throws IOException {
		// TODO Auto-generated method stub
		
		try {
            if (rq.getRequestMethod().equals("GET")) {
            
                handleGet(rq);
               
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


	
	public void handleGet(HttpExchange rq) throws IOException, a404Exception, JSONException{
		
		
		
		String rqBody = Utils.convert(rq.getRequestBody());
	
		
			JSONObject deserialized = new JSONObject(rqBody);
			
			int runtime;
			
			
			if(deserialized.has("runtime")) {
				
				
				runtime = deserialized.getInt("runtime");
				
				Neo4jConnection njc = new Neo4jConnection();
				String data = njc.getMovieByRuntime(runtime);
				njc.close();
				
				this.sendString(rq, data, 200);
			}
			
			else throw new JSONException("Missing movieId");
			
			
		
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
