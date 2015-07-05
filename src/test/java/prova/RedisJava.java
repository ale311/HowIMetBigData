package prova;

import redis.clients.jedis.Jedis;
public class RedisJava {
	public static void main(String[] args) {
		//Connecting to Redis server on localhost
		Jedis jedis = new Jedis("localhost");
		System.out.println("Connection to server sucessfully");
		//check whether server is running or not
		System.out.println("Server is running: "+jedis.ping());
		System.out.println("prova set di qualcosa: "+jedis.set("1234", "blablabla"));
		System.out.println("prova get di qualcosa: "+jedis.get("1234"));
	}
}