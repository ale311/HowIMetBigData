package prova;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;

public class TestLyrics {

	static String apiKey = "d0c4241612e7a3373d2be60d6a886bda";
	static String trackID = "81800738";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MusixMatch musixMatch = new MusixMatch(apiKey);
		String testo = getTesto(trackID,musixMatch);
		System.out.println(testo);
	}
	
	
	
	static String getTesto(String trackID, MusixMatch musixMatch){
		
		try {
			Lyrics lyrics = musixMatch.getLyrics(Integer.parseInt(trackID));
			String result = lyrics.getLyricsBody();
			return result;
		} catch (MusixMatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
