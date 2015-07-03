package prova;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.commons.io.IOUtils;

public class ProvaJSON {

	public static void main(String[] args) {
		String url = "http://api.musixmatch.com/ws/1.1/track.lyrics.get?track_id=15445219&apikey=d0c4241612e7a3373d2be60d6a886bda";

		String charset = "UTF-8";
		try {
			URLConnection connection = new URL(url).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			InputStream response = connection.getInputStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(response, writer, charset);
			String theString = writer.toString();
//			System.out.println(theString);

			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(theString);

			JSONObject jsonObject = (JSONObject) obj;
			JSONObject message = (JSONObject) jsonObject.get("message");
			JSONObject body = (JSONObject) message.get("body");
			JSONObject lyrics = (JSONObject) body.get("lyrics");
			String lyrics_body = (String) lyrics.get("lyrics_body");
			System.out.println(lyrics_body);
			
			} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
