package ca.yorku.eecs;



import static org.neo4j.driver.v1.Values.parameters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.util.Pair;



public class Neo4jConnection {
	
	//Referencing code from neo4jDemo from eclass
	
	private Driver driver;
	private String uriDb;
	
	public Neo4jConnection() {
		uriDb = "bolt://localhost:7687"; // may need to change if you used a different port for your DBMS
		Config config = Config.builder().withoutEncryption().build();
		driver = GraphDatabase.driver(uriDb, AuthTokens.basic("neo4j","123456"), config);
	}
	
	public void addActor(String name, String actorId, int age) throws JSONException, a404Exception {
		
		boolean exists = false;
		try {
			this.getActor(actorId);
			exists = true;
		} catch(a404Exception e) {
		
		System.out.println("Point 3");	
			
		try(Session ses = driver.session();) {
			ses.writeTransaction(tx -> tx.run("MERGE (a:actor {Name: $x, id: $y, age: $z})", parameters("x", name, "y", actorId, "z", age)));
			ses.close();
			System.out.println("Point 4");
		}}
		
		if (exists) throw new JSONException("actor already exists bro");
	}
	
	public void addMovie(String name, String movieId, double imdbRatings, int runtime ) throws JSONException {
		
		boolean exists = false;
		try {
			this.getMovie(movieId);
			exists = true;
		} catch(a404Exception e) {
		
		
		try(Session ses = driver.session();) {
			ses.writeTransaction(tx -> tx.run
					("MERGE (m:movie {Name: $x, id: $y, imdbRatings: $z, runtime: $v})", parameters("x", name, "y", movieId, "z", imdbRatings, "v", runtime)));
			ses.close();
		}}
		
	}
	
	public void insertRelation(String actorId, String movieId) throws JSONException, a404Exception {
			
			this.getActor(actorId);
			
			this.getMovie(movieId);
			
			
			try (Session session = driver.session())
	        {
	        	try (Transaction tx = session.beginTransaction()) {
	        		
	        	
	        		StatementResult output = tx.run("MATCH (a:actor {id: $x})-[r]->(m:movie {id: $y})"
							+ "RETURN type(r)", parameters("x", actorId, "y", movieId));
	        		
	        		System.out.println("3");
	        		if(output.hasNext()) throw new JSONException("");
	        		
	        		
	        		
	        		
	        		
	        		}
	        	}
			
			
		
			try (Session session = driver.session()){
				session.writeTransaction(tx -> tx.run("MATCH (a:actor {id:$x}),"
						+ "(m:movie {id:$y})\n" + 
						 "MERGE (a)-[r:ACTED_IN]->(m)\n" + 
						 "RETURN r", parameters("x", actorId, "y", movieId)));
				session.close();
			}
	}
	
	public String getActor(String actorId) throws JSONException, a404Exception{
        try (Session session = driver.session())
        {
        	try (Transaction tx = session.beginTransaction()) {
        		//First we return name, and age and then we return array of movieIds of the movies he/she worked  in
        		
        		StatementResult output = tx.run("MATCH (a:actor {id:$x})" + "RETURN a", parameters("x", actorId));
        		if (!output.hasNext()) throw new a404Exception();
        		
        		Record n2 = output.next();
        		Value n3 = n2.get("a");
        		
        		String actorName = n3.get("Name").asString();
        		int age = n3.get("age").asInt();

        		//now the movies worked in part
        		
        		output = tx.run("MATCH (a:actor {id: $x})-[:ACTED_IN]->(movie)\n" + "RETURN movie.id", parameters("x", actorId));
        		
        		List<String> movieIds = new ArrayList<>();
        		
        		while(output.hasNext()) {
        			Record m2 = output.next();
        			String movieId = m2.get("movie.id").asString();
        			movieIds.add(movieId);
        		}
        		
        		JSONObject jb = new JSONObject();
        		jb.put("actorId", actorId);
        		jb.put("name", actorName);
        		jb.put("age", age);
        		jb.put("movies", new JSONArray(movieIds));
        		
        		
        		return jb.toString();

        		}
        	}
        }
	
	
	public String getActorByAge(int age) throws JSONException, a404Exception{
        try (Session session = driver.session())
        {
        	try (Transaction tx = session.beginTransaction()) {
        		//First we return name, and age and then we return array of movieIds of the movies he/she worked  in
        		
        		StatementResult output = tx.run("MATCH (n) WHERE  n.age >= $x RETURN n", parameters("x", age));
        		if (!output.hasNext()) throw new a404Exception();
        		
        		List<String> actorIds = new ArrayList<>();
        		
        		while(output.hasNext()) {
        		Record n2 = output.next();
        		Value n3 = n2.get("n");
        		
        		String actorId = n3.get("actorId").asString();
        		actorIds.add(actorId);
        		
        		}
 
        		
        		JSONObject jb = new JSONObject();
        		jb.put("actors", new JSONArray(actorIds));

        		return jb.toString();

        		}
        	}
        }
    
	public String getMovie(String movieId) throws JSONException, a404Exception{
        try (Session session = driver.session())
        {
        	try (Transaction tx = session.beginTransaction()) {
        		//First we return name, ratings and runtime and then we return array of actorIds of the actors who worked in that movie
        		
        		StatementResult output = tx.run("MATCH (m:movie {id:$x})" + "RETURN m", parameters("x", movieId));
        		if (!output.hasNext()) throw new a404Exception();
        		
        		Record n2 = output.next();
        		Value n3 = n2.get("m");
        		
        		String movieName = n3.get("Name").asString();
        		double ratings = n3.get("imdbRatings").asDouble();
        		int runtime = n3.get("runtime").asInt();

        		//now the movies worked in part
        		
        		output = tx.run("MATCH (m:movie {id: $x})<-[:ACTED_IN]-(actor)\n" + "RETURN actor.id", parameters("x", movieId));
        		
        		List<String> actorIds = new ArrayList<>();
        		
        		while(output.hasNext()) {
        			Record m2 = output.next();
        			String actorId = m2.get("actor.id").asString();
        			System.out.println(actorId);
        			actorIds.add(actorId);
        		}
        		
        		
        		JSONObject jb = new JSONObject();
        		jb.put("movieId", movieId);
        		jb.put("Name", movieName);
        		jb.put("imdbRatings", ratings);
        		jb.put("runtime", runtime);
        		jb.put("Actors", new JSONArray(actorIds));
        		
        		
        		return jb.toString();

        		}
        	}
        }
	
	
	public String getMovieByRuntime(int runt) throws JSONException{
        try (Session session = driver.session())
        {
        	try (Transaction tx = session.beginTransaction()) {
        		//First we return name, and age and then we return array of movieIds of the movies he/she worked  in
        		
        		StatementResult output = tx.run("MATCH (m) WHERE  m.runtime >= $x RETURN m", parameters("x", runt));
        		
        		
        		List<String> movieIds = new ArrayList<>();
        		
        		while(output.hasNext()) {
        		Record n2 = output.next();
        		Value n3 = n2.get("m");
        		
        		String movieId = n3.get("id").asString();
        		movieIds.add(movieId);
        		System.out.println(movieId);
        		
        		}
 
        		
        		JSONObject jb = new JSONObject();
        		jb.put("Movies", new JSONArray(movieIds));

        		return jb.toString();

        		}
        	}
        }
	
	
	public String getMovieByRatings(double rate) throws JSONException{
        try (Session session = driver.session())
        {
        	try (Transaction tx = session.beginTransaction()) {
        		//First we return name, and age and then we return array of movieIds of the movies he/she worked  in
        		
        		StatementResult output = tx.run("MATCH (m) WHERE  m.imdbRatings >= $x RETURN m", parameters("x", rate));
        		
        		
        		List<String> movieIds = new ArrayList<>();
        		
        		while(output.hasNext()) {
        		Record n2 = output.next();
        		Value n3 = n2.get("m");
        		
        		String movieId = n3.get("id").asString();
        		movieIds.add(movieId);
        		
        		}
 
        		
        		JSONObject jb = new JSONObject();
        		jb.put("Movies", new JSONArray(movieIds));

        		return jb.toString();

        		}
        	}
        }
	
	
	public String hasRelationsihip(String movieId, String actorId) throws JSONException, a404Exception{
        try (Session session = driver.session())
        {
        	try (Transaction tx = session.beginTransaction()) {
        		
        		this.getActor(actorId);
        		this.getMovie(movieId);
        		StatementResult output = tx.run("MATCH (a:actor {id: $x})-[r]->(m:movie {id: $y})"
						+ "RETURN type(r)", parameters("x", actorId, "y", movieId));
        		
        		System.out.println("3");
        		if(!output.hasNext()) throw new a404Exception();
        		Value v2 = output.next().get(0);
        		System.out.println(v2.asString());
        		boolean res = false;
        		if (v2.toString().equals("\"ACTED_IN\"")) {res = true;}
        		
        		JSONObject jb = new JSONObject();
        		jb.put("actorId", actorId);
        		jb.put("movieId", movieId);
        		jb.put("hasRelatioship", res);
        		
        		return jb.toString();

        		}
        	}
        }
	
	
	public String computerBNumber(String acc) throws JSONException, a404Exception{
        try (Session session = driver.session())
        {
        	try (Transaction tx = session.beginTransaction()) {
        		//String KevinBaconId = "kb1";
        		
        		JSONObject jb = new JSONObject();
        		this.getActor(acc);
        		if(acc.equals("nm0000102")) {
        		jb.put("baconNumber", 0);
        		return jb.toString();}
        		
        		StatementResult output = tx.run("MATCH (a:actor {id: 'nm0000102'}), (b:actor {id: $x}), p = shortestPath((a)-[*]-(b))"
						+ " RETURN length(p)", parameters("x", acc));
        		
        		
        		Value v2 = output.next().get("length(p)");
        		
        		int res = v2.asInt();
        		System.out.println(res);
        		
        		if(res==1) {
        			jb.put("baconNumber", 1);
        		} else jb.put("baconNumber", res/2);
        		
        		return jb.toString();

        		}
        	}
        }
	
	
	public String computeBPath(String acc) throws JSONException, a404Exception{
        try (Session session = driver.session())
        {
        	try (Transaction tx = session.beginTransaction()) {
        		//String KevinBaconId = "kb1";
        		
        		JSONObject jb = new JSONObject();
        		this.getActor(acc);
        		
        		if(acc.equals("nm0000102")) {
        		jb.put("baconPath", "nm0000102");
        		return jb.toString();}
        		
        		StatementResult output = tx.run("MATCH (a:actor {id: $x}), (b:actor {id: 'nm0000102'}), p = shortestPath((a)-[*]-(b))"
						+ " RETURN nodes(p)", parameters("x", acc));
        		
        		
        		Value v2 = output.next().get(0);
        		
        	
        		List<String> temp = new ArrayList<>();
        		
        		for(int i=0; i< v2.size(); i++) {
        			
        			temp.add(v2.get(i).get("id").asString());
        		}
        		
        		
        		
        		for(int i=0; i<temp.size(); i++) {
        			System.out.println(temp.get(i));
        		}
        		

        		jb.put("baconPath", new JSONArray(temp));
        		
        		return jb.toString();

        		}
        	}
        }
	
	
	
	
	public void close() {
		driver.close();
	}
}