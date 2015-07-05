package prova;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.MediaSize.Engineering;

import org.jmusixmatch.MusixMatch;
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

import com.neovisionaries.i18n.CountryCode;

import scala.annotation.meta.param;
import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Event;
import de.umass.lastfm.Geo;
import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Period;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;
import de.umass.lastfm.Venue;

public class JavaAPIExample{

	private static final String username = "rj";
	private static final String apiKey ="95f57bc8e14bd2eee7f1df8595291493";
	private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";
	private static final String musicKey = "c0e18db4aa3919ba5fd2399a747b2eb9";
	public enum MieRelazioni implements RelationshipType{
		ASCOLTA, COMPONE, AMICO, VICINO, TAGGED, VIVE;
	}
	public static void main(String[] args) throws IOException {
		Date currentDate = new Date();
		System.out.println( "Starting database ... " + "TestNeo4j2Luglio "+currentDate.getHours()+":"+currentDate.getMinutes() );
		FileUtils.deleteRecursively( new File( DB_PATH ) );
		
		//avvio istanza di musicmatch
		MusixMatch musixMatch = new MusixMatch(musicKey);

		HashSet<Track> insiemeTracce = new HashSet<Track>();
		HashSet<String> insiemeArtisti = new HashSet<String>();
		HashSet<Tag> insiemeTag = new HashSet<Tag>();
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
			User userCorrente = User.getInfo(username, apiKey);
			String uname = userCorrente.getName();
			int age = userCorrente.getAge();
			String gender = userCorrente.getGender();
			String c = userCorrente.getCountry();
			int playcount = userCorrente.getPlaycount();
			CountryCode cc = CountryCode.getByCode(c);
			String country = cc.getName();
			System.out.println(country);
			queryString = "merge(u:Utente{Utente:{username}, Age:{age}, Gender:{gender}, Playcount:{playcount}})"+
						"merge(n:Nazione{Nazione:{Nazione}})"+
						"merge(u)-[:VIVE_IN]-(n)";
			parameters.put("username", username);
			parameters.put("age", age);
			parameters.put("gender", gender);
			parameters.put("playcount", playcount);
			parameters.put("Nazione", country);
			resultIterator = graphDb.execute(queryString, parameters).columnAs("utente vive in nazione");
			//fine creazione nodi country
			
			parameters.clear();

			//Accedo a lastfm per estrarre i max 200 ascolti dell'utente selezionato
//			PaginatedResult<Track> ascoltiDellUtenteDaLast = User.getRecentTracks(username, 1, 200, apiKey);
//			Collection<Track> ascoltiDellUtenteDaLast = User.getTopTracks(username, apiKey);
			Collection<Track> ascoltiDellUtenteDaLast = User.getTopTracks(username, Period.OVERALL, apiKey);
//			System.out.println(ascoltiDellUtenteDaLast.size());
			HashSet<Track> ascoltiDellUtente = new HashSet<Track>();
//			for(Track t : ascoltiDellUtenteDaLast){
//				System.out.println(t.getAlbum());
//				ascoltiDellUtente.add(t);
//			}
			for (Track tracciaCorrente : ascoltiDellUtenteDaLast){
				//inserisco le tracce ascoltate nel mio SET
				
				insiemeTracce.add(tracciaCorrente);
				//grafo: costruisco relazione tra utente e traccia, e tra traccia e Album
				String nomeTraccia = tracciaCorrente.getName();
				String nomeArtista = tracciaCorrente.getArtist();
				String mbid = tracciaCorrente.getMbid();
				int listeners= tracciaCorrente.getListeners();
				
//				String nomeAlbum = Track.getInfo(nomeArtista, nomeTraccia, apiKey).getAlbum();
//				String albumMbid = Track.getInfo(nomeArtista, nomeTraccia, apiKey).getAlbumMbid();
//				System.out.println(nomeAlbum+" "+albumMbid);
//				String mbidAlbum = tracciaCorrente.getAlbumMbid();
//				if(nomeAlbum!=null && mbidAlbum!=null){

					queryString = "merge(u:Utente{Utente:{username}})"+
							"merge(t:Traccia{Traccia:{Traccia}, MBId:{mbid}})"+	
//							"merge(alb:Album{Album:{album}, mbidalbum:{mbidalbum}})"+
							"merge(u)-[:ASCOLTA]-(t)";
//							"merge(alb)-[:CONTIENE]-(t)";

					
					parameters.put("username", username);
					parameters.put("Traccia", nomeTraccia);
					parameters.put("mbid",mbid);
//					parameters.put("listeners", listeners);
//					parameters.put("album", nomeAlbum);
//					parameters.put("mbidalbum", mbidAlbum);
					resultIterator = graphDb.execute(queryString, parameters).columnAs("utente ascolta tracce");
//				}
			}
			parameters.clear();


			//scansiono la collezione di tracce per associare la relazione COMPONE
			//con Artista, presa sempre da LAST
			for (Track tracciaCorrente : insiemeTracce){
				
				String nomeTraccia = tracciaCorrente.getName();
				String artistaTraccia = tracciaCorrente.getArtist();
				//costruisco la collezione di Artisti
				insiemeArtisti.add(tracciaCorrente.getArtist());
				
				String mbid = tracciaCorrente.getMbid();
				//grafo: costruisco relazione tra artista e traccia che ha composto
				queryString = "merge(t:Traccia{Traccia:{Traccia}, MBId:{mbid}}) "+ 
						"merge(a:Artista{Artista:{Artista}})"+
						"merge(a)-[:COMPONE]-(t)";
				parameters.put("Traccia", nomeTraccia);
				parameters.put("Artista", artistaTraccia);
				parameters.put("mbid",mbid);
				resultIterator = graphDb.execute(queryString, parameters).columnAs("artista compone tracce");

			}
			parameters.clear();
//
//			//scansiono la lista di tracce presenti nel mio SET
//			//assegno gli album alle tracce presenti nel SET
			for(Track tracciaCorrente : insiemeTracce){
				String nomeTraccia = tracciaCorrente.getName();
				String artistaTraccia = tracciaCorrente.getArtist();
//				String nomeAlbum = tracciaCorrente.getAlbum();
//				String mbidAlbum = tracciaCorrente.getAlbumMbid();
				
				//devo ricostruire la traccia: non arriva all'album, altrimenti:problemi di last
				
				String nomeAlbum = Track.getInfo(artistaTraccia, nomeTraccia, apiKey).getAlbum();
				String albumMbid = Track.getInfo(artistaTraccia, nomeTraccia, apiKey).getAlbumMbid();
				
				queryString = "merge(t:Traccia{Traccia:{Traccia}})"+
								"merge(a:Album{Album:{Album}, mbid:{mbid}})"+
								"merge(a)-[:CONTIENE]-(t)";
				parameters.put("Traccia", nomeTraccia);
				parameters.put("Album", nomeAlbum);
				parameters.put("mbid", albumMbid);
				resultIterator = graphDb.execute(queryString, parameters).columnAs("album contiene tracce");
			}
			
			parameters.clear();
			
			for(String a : insiemeArtisti){
				PaginatedResult<Event> eventiDellArtista = Artist.getEvents(a, apiKey);
				for(Event evento : eventiDellArtista){
					String nomeEvento = evento.getTitle();
					int attendant = evento.getAttendance();
					Date date = evento.getStartDate();
					//String description = evento.getDescription();
					Venue venue = evento.getVenue();
					String countryEvento = venue.getCountry();
					queryString = "merge(c:Nazione{Nazione:{Nazione}})"+
								"merge(a:Artista{Artista:{Artista}})"+
								"merge(e:Evento{Evento:{evento}, Attendant:{attendant}, Date:{date}})"+
								"merge(a)-[:PROGRAMMA_EVENTO]-(e)"+
								"merge(e)-[:IN]-(c)";
								
					parameters.put("Nazione", countryEvento);
					parameters.put("Artista", a);
					parameters.put("evento", nomeEvento);
					parameters.put("attendant", attendant);
					parameters.put("date", date.toString());
					resultIterator = graphDb.execute(queryString, parameters).columnAs("artista programma evento");
					System.out.println(venue.getCountry());
					
				}
			}
			parameters.clear();
			
			//scorro la lista delle canzoni nel SET e aggiungo il collegamento
			//ai tag associati. Limito il numero di tag a massimo 3
			
			for(Track t : insiemeTracce){
				String nomeTraccia = t.getName();
				String nomeArtista = t.getArtist();
				//forse da aggiungere mbid per la traccia
				Collection<Tag> tagAssociati = t.getTopTags(nomeArtista, nomeTraccia, apiKey);
				Iterator<Tag> it = tagAssociati.iterator(); 
				for(int i=0;i<3 && it.hasNext();i++){
					Tag currentTag = it.next();
					String currentTagToString = currentTag.getName();
					//memorizzo in un SET i tag associati ad ogni canzone
					insiemeTag.add(currentTag);
					//creo relazione traccia-tag per ogni tag
					queryString = "merge(t:Tag{Tag:{tag}})"+
							"merge(c:Traccia{Traccia:{traccia}})"+
							"merge(c)-[:HA_GENERE]-(t)";
					parameters.put("traccia", nomeTraccia);
					parameters.put("tag", currentTagToString);
					resultIterator = graphDb.execute(queryString, parameters).columnAs("traccia ha genere tag");
				}
			}
			
			parameters.clear();
			//esploro la collezione di eventi data una nazione
			//ho la certezza che sono tutti futuri
			//mi concentro solo su quelli che hanno un po' di attendent
			PaginatedResult<Event> eventiTutti = Geo.getEvents(country, "0", 1, 300, apiKey);
			for (Event evento : eventiTutti ){
				Date dataEvento = evento.getStartDate();
				int attendent = evento.getAttendance();
				if (attendent>50){
					System.out.println(evento.getAttendance());
					String nomeEvento = evento.getTitle();
					Collection<String> artistiNellEvento = evento.getArtists();
					for (String artista : artistiNellEvento){
						queryString = "merge(e:Evento{Evento:{evento},Attendent:{attendent}, Date:{date}})"+
								"merge(a:Artista{Artista:{artista}})"+	
								"merge(n:Nazione{Nazione:{nazione}})"+
								"merge(a)-[:PROGRAMMA_EVENTO]-(e)" +
								"merge(e)-[:IN]-(n)";
						parameters.put("artista", artista);
						parameters.put("nazione", country);
						parameters.put("attendent", attendent);
						parameters.put("evento", nomeEvento);
						parameters.put("date", dataEvento.toString());
						resultIterator = graphDb.execute(queryString, parameters).columnAs("artista programma evento nella nazione utente");
					}
				}
			}
		tx.success();
		}
	}
}
