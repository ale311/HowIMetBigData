package prova;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.neo4j.cypher.internal.compiler.v2_0.ast.CreateIndex;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.io.fs.FileUtils;

import de.umass.lastfm.Artist;
import de.umass.lastfm.Period;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;
import prova.*;

public class FromLAST {
	private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db/";
	private static String apiKey = "95f57bc8e14bd2eee7f1df8595291493";
	private static String username = "ale_311";
	public static void main(String[] args) throws IOException {

		System.out.println( "Starting database ..." );
		FileUtils.deleteRecursively( new File( DB_PATH ) );
//		
//		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
//		GraphDatabaseService db = dbFactory.newEmbeddedDatabase(DB_PATH);
//		
//		IndexDefinition indexDefinition;
//		
//		try (Transaction tx = db.beginTx()) {
//			 Schema schema = db.schema();
//             indexDefinition = schema.indexFor( MyLabel.ARTIST )
//                     .on( "Artist" ) 
//                     .create(); 
//             tx.success();
//		}
//		
//		try (Transaction tx = db.beginTx()) {
//			
//			User userPrin = User.getInfo(username, apiKey);
//			Node userNodePrin = db.createNode(MyLabel.USER);
//			userNodePrin.setProperty("Name", userPrin.getName());
//			userNodePrin.setProperty("Country",userPrin.getCountry());
//			userNodePrin.setProperty("Age",userPrin.getAge());
//			userNodePrin.setProperty("Gender",userPrin.getGender());
//			userNodePrin.setProperty("Playcount",userPrin.getPlaycount());
//			
//			Node countryNodePrin = db.createNode(MyLabel.COUNTRY);
//			countryNodePrin.setProperty("Nazione", userPrin.getCountry());
////			userNode1.setProperty("NumPlaylist",u1.getNumPlaylists());
//			
//			for(User userFriend : User.getFriends(username, apiKey)){
//				Node userNodeFriend =  db.createNode(MyLabel.USER);
//				userNodeFriend.createRelationshipTo(userNodePrin, MyRelationship.FRIEND);
//				userNodeFriend.setProperty("Name", userFriend.getName());
//				userNodeFriend.setProperty("Country",userFriend.getCountry());
//				userNodeFriend.setProperty("Age",userFriend.getAge());
//				userNodeFriend.setProperty("Gender",userFriend.getGender());
//				userNodeFriend.setProperty("Playcount",userFriend.getPlaycount());
//				Node countryNodeFriend = db.createNode(MyLabel.COUNTRY);
//				countryNodeFriend.setProperty("Nazione", userFriend.getCountry());
////				userNode.setProperty("NumPlaylist",u.getNumPlaylists());
//			}
//			
//			for(User sim : User.getNeighbours(userPrin.getName(), apiKey)){
//				
//				Node userNodeSim = db.createNode(MyLabel.USER);
//				userNodeSim.createRelationshipTo(userNodePrin, MyRelationship.SIMILAR);
//				userNodeSim.setProperty("Name", sim.getName());
//				//userNodeSim.setProperty("Country",sim.getCountry());
//				userNodeSim.setProperty("Age",sim.getAge());
//				//userNodeSim.setProperty("Gender",sim.getGender());
//				userNodeSim.setProperty("Playcount",sim.getPlaycount());
//				//Node countryNodeFriend = db.createNode(MyLabel.COUNTRY);
//				//countryNodeFriend.setProperty("Nazione", sim.getCountry());
//				
//			}
//			
//			for (Track t : User.getTopTracks(userPrin.getName(), apiKey)){
//				Node trackNode = db.createNode(MyLabel.TRACK);
//				trackNode.createRelationshipTo(userNodePrin, MyRelationship.LISTEN_TO);
//				trackNode.setProperty("Title", t.getName());
//				
//				String currentArtist = t.getArtist();
//				//qui devo controllare se già esiste un nodo con quel nome dell'artista 
//				//in caso, aggiungo a lui la relazione che devo aggiungere
//				Node artistNode = db.createNode(MyLabel.ARTIST);
//				artistNode.createRelationshipTo(trackNode, MyRelationship.COMPOSE);
//				artistNode.setProperty("Artist", t.getArtist());
//			}
//			tx.success();
//		}
        // END SNIPPET: dropIndex 
		System.out.println("success");
	}
	public enum MyLabel implements Label{
		USER, ARTIST, ALBUM, TAG, EVENT, TRACK, COUNTRY;
	}
	public enum MyRelationship implements RelationshipType{
		LISTEN_TO, COMPOSE, WRITE, TAGGED, PART_OF, ORGANIZE, LOCATED, LIVE_IN, FRIEND, SIMILAR;
	}
}
