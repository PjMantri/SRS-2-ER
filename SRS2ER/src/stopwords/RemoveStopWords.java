package stopwords;

import node.*;
import read_training_data_xml.ReadXML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RemoveStopWords {

	Sequence s;
	String sentence;
	// Load all the Stop   Words
	LoadStopWords load_stop_words = new LoadStopWords();

	// Remove stop words and corresponding POS tag
	public RemoveStopWords(int p) throws IOException {

		// Last inserted sequence object
		s = ReadXML.Sequences.get(ReadXML.Sequences.size() - 1);

		// English sentence
		sentence = s.sentence;

	s.pos=RemoveStopWord(sentence,s.pos);
	
	
	}

	public RemoveStopWords() {
	}

	public  Map<Integer,String> RemoveStopWord(String sentence,Map<Integer,String> pos) {

		// Split eng sentence to get individual words
		List<String> engSentences = new ArrayList<String>(
				Arrays.asList(sentence.split(" ")));

		// Iterator over eng sentence to remove stop words
		Iterator<String> i = engSentences.iterator();

		int v = -1;

		// while more words in the sentence
		while (i.hasNext()) {

			// Next word
			String s1 = i.next();

			// to calculate index of stop word to remove corresponding POS tag
			v++;

			// check from stop words loaded
			for (String j : LoadStopWords.getStopWords()) {

				// If word in the sentence is a stop word
				if (j.equalsIgnoreCase(s1)) {

					System.out.println("Removing word " + j);
					// remove English word
					i.remove();

					// remove corresponding POS tag
					pos.remove(v);

					System.out.println(" After removing at " + v + " POS sequence becomes    "
							+ pos);

					// break and check for next word in the sentnce
					break;

				}

			}

		}
		System.out.println(engSentences);
		return pos;
	}
}
