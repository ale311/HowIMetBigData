package prova;

import org.neo4j.graphdb.RelationshipType;
public enum RelTypes implements RelationshipType
{
	LISTEN_TO, COMPOSE, WRITE, TAGGED, PART_OF, ORGANIZE, LOCATED, LIVE_IN, FRIEND, SIMILAR;
}