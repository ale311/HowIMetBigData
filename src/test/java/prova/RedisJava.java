package prova;

import java.util.List;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.google.gson.JsonSyntaxException;

import redis.clients.jedis.Jedis;

public class RedisJava {

	private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";
	static String apiKey = "d0c4241612e7a3373d2be60d6a886bda";

	public static void main(String[] args) {
		// Connecting to Redis server on localhost
		// System.out.println("Connection to server sucessfully");
		// check whether server is running or not
		// System.out.println("Server is running: "+jedis.ping());
		// System.out.println("prova set di qualcosa: "+jedis.set("1234",
		// "blablabla"));
		// System.out.println("prova get di qualcosa: "+jedis.get("1234"));

		Interrogazioni interrogazione = new Interrogazioni();

		Jedis jedis = new Jedis("localhost");
		MusixMatch musixMatch = new MusixMatch(apiKey);
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
				.newEmbeddedDatabase(DB_PATH);
		List<String> ids = interrogazione.getTracce(graphDb);
		for (String id : ids) {

			Lyrics lyrics = null;
			try {
				lyrics = musixMatch.getLyrics(Integer.parseInt(id));
				String testo = lyrics.getLyricsBody();
				jedis.set(id, testo);
				System.out.println("(" + id + " , " + testo + ")\n");

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MusixMatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				
			}
		}
		System.out.println("****COMPLETED****");

	}

}