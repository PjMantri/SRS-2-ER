package read_test_data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class ReadParagraph {
	FileReader fr;
	BufferedReader br;
	String s;

	String[] sentences = new String[10];

	public ReadParagraph() {

		try {
			fr = new FileReader("paragraph.txt");
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
		}
		br = new BufferedReader(fr);

		try {

			while ((s = br.readLine()) != null) {
				// Delimiter considered is . and compared using pattern
				//matching   technique
				Pattern pat1 = Pattern.compile("[.]");
				sentences = pat1.split(s);
				for (int i = 0; i < sentences.length; i++) {

					if (!sentences[i].isEmpty()){
						System.out.println("Sentence is :" + sentences[i]);
						new POSTagTestData(sentences[i]);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
