package prova;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.MXBean;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.track.TrackData;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import com.neovisionaries.i18n.CountryCode;

import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Period;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class Methods {
	//metodo che estrae utente e sue info, crea nodo e associa a nazione
	//inoltre, restituisce la stringa NAZIONE per futuri usi, senza necessità di esplorare il grafo

	public static String aggiungiUtentiNelGrafo(GraphDatabaseService graphDb, ResourceIterator<Node> resultIterator, String username, String apiKey)  {
		//Accedo a LASTFM per estrarre nazionalità dell'utente
		User userCorrente = User.getInfo(username, apiKey);
		Map<String,Object> parameters = new HashMap<String, Object>();
		String uname = userCorrente.getName();
		int age = userCorrente.getAge();
		String gender = userCorrente.getGender();
		int playcount = userCorrente.getPlaycount();
		String c = userCorrente.getCountry();
		CountryCode cc = CountryCode.getByCode(c);
		//		String country="";
		try {
			CountryCode countryCode = CountryCode.getByCode(userCorrente.getCountry());
			String country = countryCode.getName();

			//ramo dell'if in cui l'utente ha la nazione tra le informazioni

			if(c.equals("") || cc.getName() != null || CountryCode.getByCode(c) != null){
				//			String country = "CIAONE";
				country = cc.getName();
				System.out.println(country);
				String queryString = "merge(u:Utente{Utente:{username}, Age:{age}, Gender:{gender}, Playcount:{playcount}})"+
						"merge(n:Nazione{Nazione:{Nazione}})"+
						"merge(u)-[:VIVE_IN]-(n)";
				parameters.put("username", username);
				parameters.put("age", age);
				parameters.put("gender", gender);
				parameters.put("playcount", playcount);
				parameters.put("Nazione", country);

				resultIterator = graphDb.execute(queryString, parameters).columnAs("utente vive in nazione");

				return country;

			}


			//ramo else: l'utente non ha informazioni condivise quindi non le metto
			else{
				String queryString = "merge(u:Utente{Utente:{username}, Age:{age}, Gender:{gender}, Playcount:{playcount}})";
				parameters.put("username", username);
				parameters.put("age", age);
				parameters.put("gender", gender);
				parameters.put("playcount", playcount);
				resultIterator = graphDb.execute(queryString, parameters).columnAs("utente vive in nazione");
			}

			//fine creazione nodi country

			parameters.clear();
			return "test ELSE";
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			return "test CATCH";
		}	
	}

	//implemento la logica della scelta degli utenti da restituire
	//in questa versione, restituisco solo i simili (1 SOLO SALTO, dal seme ai VICINI)
	public static Set<String> restituisciUtenti (String username, String apiKey){
		HashSet<String> result = new HashSet<String>();
		for(User u : User.getNeighbours(username, apiKey)){
			result.add(u.getName());
			//					System.out.println(u.getName());
		}
		return result;
	}


	public static void collegaUtentiAdUtentePrincipale(
			GraphDatabaseService graphDb,
			ResourceIterator<Node> resultIterator, String username,
			String userCorrente, String apikey) {
		// TODO Auto-generated method stub

		String queryString = "";
		Map<String,Object> parameters = new HashMap<String, Object>();
		queryString = "merge(u1:Utente{Utente:{username}})"+
				"merge(u2:Utente{Utente:{vicino}})"+
				"merge(u1)-[:SIMILE]-(u2)";
		parameters.put("username", username);
		parameters.put("vicino", userCorrente);
		resultIterator = graphDb.execute(queryString, parameters).columnAs("utente e suoi amici");

	}

	public static HashSet<Track>  collegaUtenteATracce(GraphDatabaseService graphDb,
			ResourceIterator<Node> resultIterator, MusixMatch musixMatch, String username,
			String apiKey) throws MusixMatchException {
		String queryString;
		HashSet<Track> insiemeTracce = new HashSet<Track>();
		Map<String,Object> parameters = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		PaginatedResult<Track> ascoltiRecentiDaLast = User.getRecentTracks(username, 1, 200, apiKey);
		Collection<Track> ascoltiTopDaLast = User.getTopTracks(username, apiKey);
		HashSet<Track> ascoltiDellUtente = new HashSet<Track>();
		for(Track t : ascoltiRecentiDaLast){
			ascoltiDellUtente.add(t);
		}
		for(Track t : ascoltiTopDaLast){
			ascoltiDellUtente.add(t);
		}
		for (Track tracciaCorrente : ascoltiDellUtente){
			//inserisco le tracce ascoltate nel mio SET
			
//			insiemeTracce.add(tracciaCorrente);
			//grafo: costruisco relazione tra utente e traccia, e tra traccia e Album
			String nomeTraccia = tracciaCorrente.getName();
			String nomeArtista = tracciaCorrente.getArtist();
			String mbid = tracciaCorrente.getMbid();
			int listeners= tracciaCorrente.getListeners();
			
			try {
				org.jmusixmatch.entity.track.Track track = musixMatch.getMatchingTrack(nomeTraccia, nomeArtista);
				TrackData data = track.getTrack();
				int mxid = data.getTrackId();
				queryString = "merge(u:Utente{Utente:{username}})"+
						"merge(t:Traccia{Traccia:{Traccia}, MBId:{mbid}, MusixMatchId:{mxid}})"+	
						"merge(u)-[:ASCOLTA]-(t)";
				parameters.put("username", username);
				parameters.put("Traccia", nomeTraccia);
				parameters.put("mbid",mbid);
				parameters.put("mxid", mxid);
				resultIterator = graphDb.execute(queryString, parameters).columnAs("utente ascolta tracce");
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		parameters.clear();
		return ascoltiDellUtente;
	}
}
