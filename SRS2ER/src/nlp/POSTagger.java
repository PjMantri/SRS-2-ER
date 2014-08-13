/*
 * Singleton implementation
 */

package nlp;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
/**
 * 
 * @author Suresh Sarda, Pooja Mantri
 *
 */
public class POSTagger {
	private LexicalizedParser lp = LexicalizedParser.loadModel("Tagger\\englishPCFG.ser.gz");
	private static POSTagger instance = null;
	private POSTagger() {
		
	}
	public static POSTagger getInstance() {
		if (instance == null) {
			instance = new POSTagger();
		}
		return instance;
	}
	public List<WordToken> TagSentence(String sentence) {
		String[] words = sentence.split(" ");
		String[] post = (String[]) Tag(sentence).toArray();
		
		List<WordToken> tokens = new ArrayList<WordToken>();
		for (int i = 0; i < words.length; i++) {
			WordToken token = new WordToken();
			token.wordId = i;
			token.word = words[i];
			token.post = post[i];
			
			tokens.add(token);
		}
		
		return tokens;
	}
	
	
	private List<String> Tag(String sentence) {
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
