package prova;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

import scala.Array;

public class Interrogazioni {

	private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";

	public static void main(String[] args) {

		GraphDatabaseService graphDb = new GraphDatabaseFactory()
				.newEmbeddedDatabase(DB_PATH);

		String utente = "rj";
		String traccia = "Steamroller";
		String album = "Unelectric";
		String evento = "Brian Wilson";
		String tag = "dub";
		String nazione = "France";
		String artista = "Rodriguez";

		Interrogazioni get = new Interrogazioni();

		// get.getAll(graphDb);
		// get.getUtenti(graphDb);
		get.getTracce(graphDb);
		// get.getAlbums(graphDb);
		// get.getEventi(graphDb);
		// get.getTags(graphDb);
		// get.getNazioni(graphDb);
		// get.getArtisti(graphDb);
		//
		// get.getUtente(graphDb, utente);
		// get.getTraccia(graphDb, traccia);
		// get.getAlbum(graphDb, album);
		// get.getEvento(graphDb, evento);
		// get.getTag(graphDb, tag);
		// get.getNazione(graphDb, nazione);
		// get.getArtista(graphDb, artista);
		//
		// get.countAll(graphDb);
		// get.countUtenti(graphDb);
		// get.countTracce(graphDb);
		// get.countAlbums(graphDb);
		// get.countEventi(graphDb);
		// get.countTags(graphDb);
		// get.countNazioni(graphDb);
		// get.countArtisti(graphDb);
		//
		// get.getUserTracks(graphDb, utente);
		//

	}

	public void getAll(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine.execute("MATCH(n) RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);
		System.out.println("GET ALL OK");
	}

	public void getUtenti(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Utente) RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET UTENTI OK");
	}

	public List<String> getTracce(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Traccia) RETURN n.MusixMatchId");

		ResourceIterator<Map<String, Object>> tracce = execResult.iterator();

		List<String> ids = new ArrayList<String>();
		for (Map<String, Object> node : IteratorUtil.asIterable(tracce)) {
			Collection<Object> values = node.values();
			for (Object v : values) {
				if (v != null) {
					ids.add(v.toString());
				}
			}
		}
		return ids;
	}

	public void getAlbums(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Album) RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET ALBUMS OK");

	}

	public void getEventi(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Evento) RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET EVENTI OK");

	}

	public void getNazioni(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Nazione) RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET NAZIONI OK");

	}

	public void getTags(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Tag) RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET TAGS OK");

	}

	public void getArtisti(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Artista) RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET ARTISTI OK");
	}

	public void getUtente(GraphDatabaseService graphDb, String utente) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Utente) WHERE n.Utente='" + utente
						+ "' RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET UTENTE OK");

	}

	public void getTraccia(GraphDatabaseService graphDb, String traccia) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Traccia) WHERE n.Traccia='" + traccia
						+ "' RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET TRACCIA OK");

	}

	public void getAlbum(GraphDatabaseService graphDb, String album) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Album) WHERE n.Album='" + album
						+ "' RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET ALBUM OK");

	}

	public void getEvento(GraphDatabaseService graphDb, String evento) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Evento) WHERE n.Evento='" + evento
						+ "' RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET EVENTO OK");

	}

	public void getNazione(GraphDatabaseService graphDb, String nazione) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Nazione) WHERE n.Nazione='" + nazione
						+ "' RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET NAZIONE OK");

	}

	public void getTag(GraphDatabaseService graphDb, String tag) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Tag) WHERE n.Tag='" + tag + "' RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET TAG OK");

	}

	public void getArtista(GraphDatabaseService graphDb, String artista) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Artista) WHERE n.Artista='" + artista
						+ "' RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET ARTISTA OK");

	}

	public void countAll(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n) RETURN count(n)");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("COUNT ALL OK");

	}

	public void countUtenti(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Utente) RETURN count(n)");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("COUNT UTENTI OK");

	}

	public void countTracce(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Traccia) RETURN count(n)");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("COUNT TRACCE OK");

	}

	public void countAlbums(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Album) RETURN count(n)");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("COUNT ALBUMS OK");

	}

	public void countEventi(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Evento) RETURN count(n)");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("COUNT EVENTI OK");

	}

	public void countNazioni(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Nazione) RETURN count(n)");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("COUNT NAZIONI OK");

	}

	public void countTags(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Tag) RETURN count(n)");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("COUNT TAGS OK");

	}

	public void countArtisti(GraphDatabaseService graphDb) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH(n:Artista) RETURN count(n)");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("COUNT UTENTI OK");

	}

	public void getUserTracks(GraphDatabaseService graphDb, String utente) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine
				.execute("MATCH (n:Traccia)<-[r]-(u:Utente {Utente:'" + utente
						+ "'}) RETURN n");
		String results = execResult.dumpToString();
		System.out.println(results);

		System.out.println("GET USER-TRACKS OK");

	}
}
