package prova;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import redis.clients.jedis.Jedis;
public class RedisJava {
	
	private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";
	
	public static void main(String[] args) {
		//Connecting to Redis server on localhost
//		System.out.println("Connection to server sucessfully");
		//check whether server is running or not
//		System.out.println("Server is running: "+jedis.ping());
//		System.out.println("prova set di qualcosa: "+jedis.set("1234", "blablabla"));
//		System.out.println("prova get di qualcosa: "+jedis.get("1234"));

		
		Jedis jedis = new Jedis("localhost");

		Interrogazioni interrogazione = new Interrogazioni();
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
		.newEmbeddedDatabase(DB_PATH);
		List<String> ids = interrogazione.getTracce(graphDb);
		for(String id : ids) {
			String testo = "prova";
			jedis.set(id, testo);
			System.out.println("(" + id + " , " + testo + ")\n");

		}
		
		
	}
	
	
}