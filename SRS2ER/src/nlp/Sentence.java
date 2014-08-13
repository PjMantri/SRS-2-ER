package nlp;

import java.util.ArrayList;
import java.util.List;

import stopwords.LoadStopWords;

/**
 * Sentence contains the current sentence which is being processed.
 * @author Suresh Sarda
 *
 */
public class Sentence {
	/*Actual sentence*/
	private String sentence = new String();
	
	/*Sentence in terms of individual tokens*/
	private List<WordToken> tokens = new ArrayList<WordToken>();
	private List<WordToken> processedTokens = new ArrayList<WordToken>();

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		/*Things to do when a sentence is changed:
		 * 1. Update POST
		 * 2. Remove stop words
		 */
		this.sentence = sentence;
		tokens = POSTagger.getInstance().TagSentence(sentence);
		processedTokens = LoadStopWords.RemoveStopWords(tokens);
		
	}
	public List<WordToken> getProcessedTokens() {
		return processedTokens;
	}
	public List<WordToken> getTokens() {
		return tokens;
	}
}
