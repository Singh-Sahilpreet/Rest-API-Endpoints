package ca.yorku.eecs;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

//Reference - https://docs.oracle.com/en/java/javase/17/docs/api/jdk.httpserver/com/sun/net/httpserver/package-summary.html
//Reference - Basichttpserver from eclasss

public class App {
    static int PORT = 8080;
    public static void main(String[] args) throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
       
        //server urls
        
        
        server.createContext("/api/v1/addActor", new AddActor());
        server.createContext("/api/v1/addMovie", new AddMovie());
        server.createContext("/api/v1/addRelationship", new addRelationship());
        
        server.createContext("/api/v1/getMovie", new GetMovie());
        server.createContext("/api/v1/getMovieByRuntime", new GetMovieByRuntime());
        server.createContext("/api/v1/getMovieByRatings", new GetMovieByRatings());
        
        server.createContext("/api/v1/getActor", new GetActor());
        server.createContext("/api/v1/getActorByAge", new GetActorByAge());
        server.createContext("/api/v1/hasRelationship", new HasRelationship());
        
        server.createContext("/api/v1/computeBaconPath", new GetBaconPath());
        server.createContext("/api/v1/computeBaconNumber", new GetBaconNumber());
        
        
        server.start();
        System.out.printf("Server is running");
    }
}
