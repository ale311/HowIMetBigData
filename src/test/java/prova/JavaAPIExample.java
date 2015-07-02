package prova;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import scala.annotation.meta.param;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Event;
import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;
import de.umass.lastfm.Venue;

public class JavaAPIExample{

	private static final String username = "ale_311";
	private static final String apiKey ="95f57bc8e14bd2eee7f1df8595291493";
	private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";

	public enum MieRelazioni implements RelationshipType{
		ASCOLTA, COMPONE, AMICO, VICINO, TAGGED, VIVE;
	}
	public static void main(String[] args) throws IOException {
		Date currentDate = new Date();
		System.out.println( "Starting database ... " + currentDate.getTime() );
		FileUtils.deleteRecursively( new File( DB_PATH ) );

		HashSet<Track> insiemeTracce = new HashSet<Track>();
		HashSet<String> insiemeArtisti = new HashSet<String>();
		// START SNIPPET: startDb 
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );

		ResourceIterator<Node> resultIterator = null;

		try (Transaction tx = graphDb.beginTx()){
			Label labelUtente = DynamicLabel.label("Utente");
			Label labelTraccia = DynamicLabel.label("Traccia");
			Label labelArtista = DynamicLabel.label("Artista");
			String queryString ="";
			Map<String,Object> parameters = new HashMap<String, Object>();
			//		
			//		//Accedo a last per estrarre gli amici dell'utente selezionato
			//		PaginatedResult<User> amici = User.getFriends(username, apiKey);
			//		//costruisco relazione tra vicini e utente
			//		for(User u : amici){
			//			String friendName = u.getName();
			//			
			//			queryString = "merge(u1:Utente{Utente:{username}})"+
			//							"merge(u2:Utente{Utente:{vicino}})"+
			//							"merge(u1)-[:AMICO]-(u2)";
			//			parameters.put("username", username);
			//			parameters.put("vicino", friendName);
			//			resultIterator = graphDb.execute(queryString, parameters).columnAs("utente e suoi amici");
			//		
			//		}
			//		parameters.clear();
			//		//scorro la collezione dei vicini e li collego alle tracce ascoltate da loro
			//		for(User u : amici){
			//			String friendName = u.getName();
			//			PaginatedResult<Track> ascoltiDeiVicini = User.getRecentTracks(friendName, 1, 200, apiKey);
			//			for (Track tracciaCorrente : ascoltiDeiVicini){
			//				//inserisco la traccia nel mio SET
			//				insiemeTracce.add(tracciaCorrente);
			//				//collego l'utente alla traccia
			//				String nomeTraccia = tracciaCorrente.getName();
			//				queryString = "merge(u:Utente{Utente:{username}})"+
			//								"merge(t:Traccia{Traccia:{Traccia}})"+	
			//								"merge(u)-[:ASCOLTA]-(t)";
			//				parameters.put("username", friendName);
			//				parameters.put("Traccia", nomeTraccia);
			//				resultIterator = graphDb.execute(queryString, parameters).columnAs("utente ascolta tracce");
			//			}
			//			
			//		}

			parameters.clear();
			//Accedo a LASTFM per estrarre nazionalità dell'utente
			User user = User.getInfo(username, apiKey);
			String uname = user.getName();
			String country = user.getCountry();
			queryString = "merge(u:Utente{Utente:{username}})"+
						"merge(n:Nazione{Nazione:{Nazione}})"+
						"merge(u)-[:VIVE_IN]-(n)";
			parameters.put("username", username);
			parameters.put("Nazione", country);
			resultIterator = graphDb.execute(queryString, parameters).columnAs("utente vive in nazione");
			//fine creazione nodi country
			
			parameters.clear();

			//Accedo a lastfm per estrarre i max 200 ascolti dell'utente selezionato
			PaginatedResult<Track> ascoltiDellUtente = User.getRecentTracks(username, 1, 200, apiKey);
			for (Track tracciaCorrente : ascoltiDellUtente){
				//inserisco le tracce ascoltate nel mio SET
				insiemeTracce.add(tracciaCorrente);
				//grafo: costruisco relazione tra utente e traccia
				String nomeTraccia = tracciaCorrente.getName();
				queryString = "merge(u:Utente{Utente:{username}})"+
						"merge(t:Traccia{Traccia:{Traccia}})"+	
						"merge(u)-[:ASCOLTA]-(t)";
				parameters.put("username", username);
				parameters.put("Traccia", nomeTraccia);
				resultIterator = graphDb.execute(queryString, parameters).columnAs("utente ascolta tracce");
			}
			parameters.clear();


			//scansiono la collezione di tracce per associare la relazione COMPONE
			//con Artista, presa sempre da LAST
			for (Track tracciaCorrente : insiemeTracce){
				
				String nomeTraccia = tracciaCorrente.getName();
				String artistaTraccia = tracciaCorrente.getArtist();
				//costruisco la collezione di Artisti
				insiemeArtisti.add(tracciaCorrente.getArtist());
				
				String mbidSTRING = tracciaCorrente.getMbid();
				//grafo: costruisco relazione tra artista e traccia che ha composto
				queryString = "merge(t:Traccia{Traccia:{Traccia}}) "+ 
						"merge(a:Artista{Artista:{Artista}})"+
						"merge(a)-[:COMPONE]-(t)";
				parameters.put("Traccia", nomeTraccia);
				parameters.put("Artista", artistaTraccia);
				resultIterator = graphDb.execute(queryString, parameters).columnAs("artista compone tracce");

			}
			parameters.clear();

			//scansiono la lista di tracce presenti nel mio SET
			//assegno gli album alle tracce presenti nel SET
			for(Track tracciaCorrente : insiemeTracce){
				String nomeTraccia = tracciaCorrente.getName();
				String artistaTraccia = tracciaCorrente.getArtist();
				String nomeAlbum = tracciaCorrente.getAlbum();
				String mbidAlbum = tracciaCorrente.getAlbumMbid();
				
				queryString = "merge(t:Traccia{Traccia:{Traccia}})"+
								"merge(a:Album{Album:{Album}})"+
								"merge(a)-[:CONTIENE]-(t)";
				parameters.put("Traccia", nomeTraccia);
				parameters.put("Album", nomeAlbum);
				resultIterator = graphDb.execute(queryString, parameters).columnAs("album contiene tracce");
			}
			
			for(String a : insiemeArtisti){
				PaginatedResult<Event> eventiDellArtista = Artist.getEvents(a, apiKey);
				for(Event evento : eventiDellArtista){
					String description = evento.getDescription();
					Venue venue = evento.getVenue();
					
					System.out.println(venue.getCountry());
					System.out.println(venue.getId());
					System.out.println(venue.getName());
					System.out.println(venue.getPostal());
				}
			
			}
		
		tx.success();
	}

}
}