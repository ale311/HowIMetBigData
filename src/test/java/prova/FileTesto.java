package prova;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileTesto {

	public static void main(String[] args) throws IOException {
		String path = "/Users/FrappoMacBook/git/TestNeo4j2Luglio/util/html.txt";

		try {
			File file = new File(path);
			FileWriter w = new FileWriter(path);
			BufferedWriter b = new BufferedWriter(w);
			b.write("babu babu");
			b.flush();
			b.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
