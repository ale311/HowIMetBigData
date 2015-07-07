package prova;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.tools.Tool;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
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
	private static final String mxapiKey = "d0c4241612e7a3373d2be60d6a886bda";
	
	
	
	public static void main (String [] args) throws IOException, MusixMatchException{
		Date currentDate = new Date();
		System.out.println( "Starting database " + "Beyond Big Data "+currentDate.getHours()+":"+currentDate.getMinutes() );
		FileUtils.deleteRecursively( new File( DB_PATH ) );

		//avvio istanza di musicmatch
		MusixMatch musixMatch = new MusixMatch(mxapiKey);
		//istanziate le strutture dati utili in RAM per mantenere memoria dei dati estratti 
		
		HashSet<String> countries = new HashSet<String>();
		HashSet<Track> insiemeTracce = new HashSet<Track>();
		HashSet<String> insiemeArtisti = new HashSet<String>();
		HashSet<Tag> insiemeTag = new HashSet<Tag>();
		HashSet<String> insiemeUtenti = new HashSet<String>();
		HashSet<Integer> insiemeTrackID = new HashSet<Integer>();
		HashSet<Track> insiemeTracceDeiTag = new HashSet<Track>();
		
		
		// START SNIPPET: startDb 
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );

		ResourceIterator<Node> resultIterator = null;

		//salvo utente principale e nodo nazione
		//memorizzo nazione in set, per usarla poi per gli eventi
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			String country = Methods.aggiungiUtentiNelGrafo(graphDb, resultIterator, countries, username, apiKey);
			countries.add(country);
			tx.success();
		}
		
		//DA DECOMMENTARE!
		
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			insiemeUtenti = (HashSet<String>) Methods.restituisciUtenti(username, apiKey);
			for(String userCorrente : insiemeUtenti){
//				System.out.println(userCorrente);
//				System.out.println(insiemeUtenti.size());
				Methods.aggiungiUtentiNelGrafo(graphDb, resultIterator, countries, userCorrente, apiKey);
				Methods.collegaUtentiAdUtentePrincipale (graphDb, resultIterator ,username, userCorrente, apiKey);
			}
			tx.success();
		}
		
		//aggiungo tracce preferite dall'utente principale
		
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			try {
				Methods.collegaUtenteATracce(graphDb, resultIterator,musixMatch, insiemeTracce, insiemeTrackID, username, apiKey);
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				tx.success();
			}
		}

	//TODO verificare utilità di questo metodo, non dovrebbe essere più utile
//		System.out.println(insiemeTrackID.size());
		//aggiungo le tracce dei simili ai simili
//		try (Transaction tx = graphDb.beginTx()){
//			String queryString ="";
//			Map<String,Object> parameters = new HashMap<String, Object>();
////			insiemeUtenti = (HashSet<String>) Methods.restituisciUtenti(username, apiKey);
//			for(String userCorrente : insiemeUtenti){
//				Methods.collegaUtenteATracce(graphDb, resultIterator, userCorrente, apiKey);
//			}
//			tx.success();
//		}
////		
		int j = 0;
		for(String userCorrente : insiemeUtenti){
			
			System.out.println("collego tracce a utente "+userCorrente+ " " + j++);
			try (Transaction tx = graphDb.beginTx()){
				try {
					Methods.collegaUtenteATracce(graphDb, resultIterator,musixMatch,insiemeTracce, insiemeTrackID, userCorrente, apiKey);
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					tx.success();
				}
			}
		}
		
		
		System.out.println(insiemeTracce.size());
		
		
		
		
		//aggiungo TAG corrispondenti alle tracce che ho nel mio insieme: 
		//passo l'insieme delle trackid e chiedo a mx di restituirmi un tag (Speriamo sia più preciso di last)
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			try {
				Methods.collegaTracceAiTag (graphDb,resultIterator, musixMatch,insiemeTag, insiemeTracce, username, apiKey);
//				Methods.collegaUtenteATracce(graphDb, resultIterator,musixMatch, insiemeTrackID, username, apiKey);
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				tx.success();
			}
		}
		
		//estraggo topTrack dei GENERI (TAG)
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			try {
				Methods.estraiTopTrackDaTag(graphDb, resultIterator,musixMatch, insiemeTag, insiemeTracce, insiemeTrackID, username, apiKey);
//				Methods.collegaTracceAiTag (graphDb,resultIterator, musixMatch,insiemeTag, insiemeTracce, username, apiKey);
//				Methods.collegaUtenteATracce(graphDb, resultIterator,musixMatch, insiemeTrackID, username, apiKey);
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				tx.success();
			}
		}
		
		//aggiungo artisti nel mio insieme e li collego alle tracce
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			try {
				Methods.collegaTracceAdArtisti (graphDb,resultIterator, musixMatch, insiemeTracce,insiemeArtisti, username, apiKey);
//				Methods.collegaUtenteATracce(graphDb, resultIterator,musixMatch, insiemeTrackID, username, apiKey);
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				tx.success();
			}
		}
		System.out.println(insiemeArtisti.size());
		
		//dalle nazioni degli utenti, estraggo i concerti eventi e li associo alla nazione e ad artisti
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			try {
				Methods.estraiEventiDaNazioni (graphDb,resultIterator,countries, username, apiKey);
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				tx.success();
			}
		}
		
		//dagli artisti presenti nel sistema, estraggo i loro futuri eventi e li associo a nazioni e artisti
		try (Transaction tx = graphDb.beginTx()){
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			try {
				Methods.estraiEventiDaArtisti (graphDb,resultIterator,insiemeArtisti, username, apiKey);
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				tx.success();
			}
		}
		
	}
}