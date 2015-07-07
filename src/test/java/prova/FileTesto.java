package prova;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import redis.clients.jedis.Jedis;

public class FileTesto {

	private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";

	public static void main(String[] args) throws IOException {

		Jedis jedis = new Jedis("localhost");
		Interrogazioni interrogazioni = new Interrogazioni();
		FileTesto testo = new FileTesto();
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
				.newEmbeddedDatabase(DB_PATH);

		testo.testiUtente(graphDb, interrogazioni, jedis);
		testo.testiGenere(graphDb, interrogazioni, jedis);

	}

	public void testiUtente(GraphDatabaseService graphDb,
			Interrogazioni interrogazioni, Jedis jedis) {
		String path = "/Users/FrappoMacBook/git/TestNeo4j2Luglio/util/testiUtente.txt";
		String utente = "ale_311";
		List<String> ids = interrogazioni.getUserTracks(graphDb, utente);

		try {
			File file = new File(path);
			FileWriter w = new FileWriter(path);
			BufferedWriter b = new BufferedWriter(w);
			for (String id : ids) {
				String result = jedis.get(id);
				result = result.replace(',', ' ');
				result = result.replace('(', ' ');
				result = result.replace(')', ' ');
				result = result.replace('\'', ' ');
				result = result.replace('.', ' ');
				result = result.replace(' ', '\n');
				result = result.toLowerCase();

				b.write(result);
			}
			b.flush();
			b.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void testiGenere(GraphDatabaseService graphDb,
			Interrogazioni interrogazioni, Jedis jedis) {
		String path = "/Users/FrappoMacBook/git/TestNeo4j2Luglio/util/testiGenere.txt";
		String tag = "rock";
		List<String> ids = interrogazioni.getTagTracks(graphDb, tag);

		try {
			File file = new File(path);
			FileWriter w = new FileWriter(path);
			BufferedWriter b = new BufferedWriter(w);
			for (String id : ids) {
				String result = jedis.get(id);
				result = result.replace(',', ' ');
				result = result.replace('(', ' ');
				result = result.replace(')', ' ');
				result = result.replace('\'', ' ');
				result = result.replace('.', ' ');
				result = result.replace(' ', '\n');
				result = result.toLowerCase();

				b.write(result);
			}
			b.flush();
			b.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
