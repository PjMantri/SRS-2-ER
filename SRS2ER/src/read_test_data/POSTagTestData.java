package read_test_data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import stopwords.RemoveStopWords;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

public class POSTagTestData {

	Map<Integer, String> POS = new HashMap<Integer, String>();
	Collection<String> POSSequence;
	Set<Integer> IndexSequence;
	static LexicalizedParser lp = LexicalizedParser
			.loadModel("Tagger\\englishPCFG.ser.gz");
	int m;

	public POSTagTestData(String TestSentence) {

		// POS tag sentence using Stanford  tagger

		List<String> sen = Tag(TestSentence);

		int h = sen.size();

		for (m = 0; m < h; m++)
			POS.put(m, sen.get(m));

		System.out.println(POS);

		RemoveStopWords rs = new RemoveStopWords();

		POS = rs.RemoveStopWord(TestSentence, POS);

		System.out.println(POS);

		POSSequence = POS.values();
		IndexSequence = POS.keySet();

		//System.out.println(POSSequence);
		//System.out.println();
		//System.out.println(IndexSequence);

		// ER tag Test Data

		try {
			new ERTagTestData(TestSentence, POSSequence, IndexSequence);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<String> Tag(String sentence) {
		String[] sent = sentence.split(" ");
		List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
		Tree parse = lp.apply(rawWords);
		List<CoreLabel> tagged = parse.taggedLabeledYield();
		List<String> tags = new ArrayList<String>();
		for (CoreLabel token : tagged) {
			tags.add(token.toString().split("-")[0]);
		}
		return tags;
	}

}
