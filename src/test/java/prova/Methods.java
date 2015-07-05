package prova;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import com.neovisionaries.i18n.CountryCode;

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
			return "CIAONE";
		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
			return "kitemmuort";
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
				"merge(u1)-[:AMICO]-(u2)";
		parameters.put("username", username);
		parameters.put("vicino", userCorrente);
		resultIterator = graphDb.execute(queryString, parameters).columnAs("utente e suoi amici");

	}

	
	
	


}
