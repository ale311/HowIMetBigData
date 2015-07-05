package prova;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jmusixmatch.MusixMatch;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class BeyondBigData {
//	private static final String username = "rj";
	private static final String username = "ale_311";
	private static final String apiKey ="95f57bc8e14bd2eee7f1df8595291493";
	private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";
	private static final String musicKey = "c0e18db4aa3919ba5fd2399a747b2eb9";

	public static void main (String [] args) throws IOException{
		Date currentDate = new Date();
		System.out.println( "Starting database " + "Beyond Big Data "+currentDate.getHours()+":"+currentDate.getMinutes() );
		FileUtils.deleteRecursively( new File( DB_PATH ) );

		//avvio istanza di musicmatch
		MusixMatch musixMatch = new MusixMatch(musicKey);
		HashSet<String> countries = new HashSet<String>();
		HashSet<Track> insiemeTracce = new HashSet<Track>();
		HashSet<String> insiemeArtisti = new HashSet<String>();
		HashSet<Tag> insiemeTag = new HashSet<Tag>();
		// START SNIPPET: startDb 
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );

		ResourceIterator<Node> resultIterator = null;

		//salvo utente principale e nodo nazione
		//memorizzo nazione in set, per usarla poi per gli eventi
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			String country = Methods.aggiungiUtentiNelGrafo(graphDb, resultIterator , username, apiKey);
			countries.add(country);
			tx.success();
		}
		
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			Set<String> utenti = Methods.restituisciUtenti(username, apiKey);
			for(String userCorrente : utenti){
				System.out.println(userCorrente);
				System.out.println(utenti.size());
				Methods.aggiungiUtentiNelGrafo(graphDb, resultIterator, userCorrente, apiKey);
				Methods.collegaUtentiAdUtentePrincipale (graphDb, resultIterator ,username, userCorrente, apiKey);
			}
			tx.success();
		}
	}
}