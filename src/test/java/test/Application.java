package test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.TrackData;

import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class Application {
	private static final String username = "rj";
	private static final String apiKey ="95f57bc8e14bd2eee7f1df8595291493";
	static String mxapiKey = "d0c4241612e7a3373d2be60d6a886bda";
	static MusixMatch musixMatch = new MusixMatch(mxapiKey);
	public static void main(String[] args) throws MusixMatchException {
		// TODO Auto-generated method stub
		HashSet<Tag> insiemeTag = new HashSet<Tag>();
		User user = User.getInfo(username, apiKey);
		String uname = user.getName();
		int age = user.getAge();
		String gender = user.getGender();
		String c = user.getCountry();
		int playcount = user.getPlaycount();
		Locale countryLocale = new Locale ("", c);
		int j  = 0;
		String country = countryLocale.getDisplayCountry(Locale.ENGLISH);
//		PaginatedResult<Track> ascoltiDellUtente = User.getRecentTracks(username, 1, 200, apiKey);
		Collection<Track> ascoltiDellUtente = User.getTopTracks(username, apiKey);
		for (Track tracciaCorrente : ascoltiDellUtente){
			System.out.println(j++ + tracciaCorrente.getMbid());
			String nomeTraccia = tracciaCorrente.getName();
			String artistaTraccia = tracciaCorrente.getArtist();
			System.out.println(artistaTraccia);
			org.jmusixmatch.entity.track.Track track = musixMatch.getMatchingTrack(nomeTraccia, artistaTraccia);
			TrackData data = track.getTrack();
			Lyrics lyrics = musixMatch.getLyrics(data.getTrackId());
			System.out.println(lyrics.getLyricsBody());
			System.out.println("====================================");
			
//			Collection<Tag> tagAssociati = tracciaCorrente.getTopTags(artistaTraccia, nomeTraccia, apiKey);
//			Iterator<Tag> it = tagAssociati.iterator(); 
//			for(int i=0;i<2 && it.hasNext();i++){
//				Tag currentTag = it.next();
//				insiemeTag.add(currentTag);
//				System.out.println(nomeTraccia+" "+": "+artistaTraccia+" "+currentTag.getName());
//			}
		}
		
		
	}

}
