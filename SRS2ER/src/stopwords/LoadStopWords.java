package stopwords;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nlp.WordToken;


/**
 * LoadStopWords - Methods to load stop words into memory.
 * There is no need to create instance of this class. Stop words are stored in a static location and
 * Methods required to remove stop words are also static. Though instances can be created and used
 * 
 * 
 * @author Pooja Mantri (Edited by Suresh Sarda)
 *
 */

public class LoadStopWords {
	/**
	 * List of stop words 
	 */
	
	// TODO Make non static
	private static List<String> StopWords;

	/**
	 * Default constructor
	 * When no parameter is passed, it tries to load stopwords form file "stopWords.csv".
	 */
	public LoadStopWords() {
		this("stopwords.csv");
	}
	public LoadStopWords(String filename) {
		
		setStopWords(new ArrayList<String>());
		
		try {
			Load(filename);	
		}
		catch (FileNotFoundException e) {
			System.err.println("Error: File containing stopwords was not found. Continueing without.");
		}
		catch (IOException e) {
			System.err.println("Error: Unknown error occured while reading stop words. Continuing without.");
			System.err.println("More Detials:");
			e.printStackTrace();
		}
		
	}

	/**
	 * Loads stop words from a file. The file should be in Comma Separted Value(CSV) format
	 * @param filename the name of file from where to load stop words
	 * @throws IOException 
	 * @see FileNotFoundException
	 */
	public static void Load(String filename) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = new String();
		
		while ((line = br.readLine()) != null) {
			List<String> words = new ArrayList<String>();
			words = Arrays.asList(line.split(","));
			getStopWords().addAll(words);
		}
		br.close();
	}
	
	/**
	 * Remove stop words form sentence made up of WordToken
	 * <p>This function does not make changes to the passed list. New list should be stored.
	 * @param words list of WordToken
	 * @return updated list of WordToken
	 */
	public static List<WordToken> RemoveStopWords(List<WordToken> words) {
		List<WordToken> temp = new ArrayList<WordToken>();
		temp.addAll(words);
		
		/*Iterate through all the words and remove search stop wrods to remove*/
		for (int i = 0; i < temp.size(); i++) {
			/*if the current word is in stop words list, then remove it*/
			if (getStopWords().contains(temp.get(i).word.toLowerCase())) {
				temp.remove(i);
			}
		}
		return temp;
	}
	
	/**
	 * Get list of stop words
	 * @return List of strings
	 */
	public static List<String> getStopWords() {
		return StopWords;
	}
	
	/**
	 * Set stop word list
	 * @param stopWords list of string
	 */
	private static void setStopWords(List<String> stopWords) {
		StopWords = stopWords;
	}

}
