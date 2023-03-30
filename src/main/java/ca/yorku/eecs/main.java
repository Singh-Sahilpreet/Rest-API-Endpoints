package ca.yorku.eecs;

import org.json.JSONException;

public class main {

	public static void main(String[] args) throws a404Exception {
		// TODO Auto-generated method stub
		
		Neo4jConnection nj = new Neo4jConnection();
		
		
		try {
			nj.computeBPath("nm31");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nj.close();
	}

}
