package sentence_simplification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

/*
 * 
 * TryStanfordParser Converts Complex and compound sentences to simple
 * @author Pooja Mantri
 * @methodExplained http://stackoverflow.com/questions/9595983/tools-for-text-simplification-java
 * 
 */
public class TryStanfordParser {
	static LexicalizedParser lp = LexicalizedParser.loadModel(
			"Tagger/englishPCFG.ser.gz", "-maxLength", "80",
			"-retainTmpSubcategories");
	TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	TypedDependency t, subject, t1;

	static String w = "";
	String complexSentence;
	String[] sent;
	Tree parse;
	Boolean present = false;
	GrammaticalStructure gs;
	String line;

	Collection<TypedDependency> tdl;
	// To process typed dependencies

	Collection<TypedDependency> clauses = new ConcurrentLinkedQueue<TypedDependency>();
	Collection<TypedDependency> subjects = new ConcurrentLinkedQueue<TypedDependency>();

	// List of simple sentences
	List<String> simpleSentences = new ArrayList<>();

	public TryStanfordParser() throws IOException {
		try {

			BufferedReader br = new BufferedReader(new FileReader(
					"compound.txt"));

			while ((line = br.readLine()) != null) {
				complexSentence = line;

				sent = complexSentence.split(" ");
				parse = lp.apply(Sentence.toWordList(sent));
				gs = gsf.newGrammaticalStructure(parse);

				tdl = gs.typedDependenciesCCprocessed();
				getSubjects();

				readSubject();
				clear();

			}
			br.close();

		} catch (FileNotFoundException e) {
			System.err.println("Test Data File Not Found");
		}
	}

	public void clear() {
		subjects.clear();
		clauses.clear();
		simpleSentences.clear();

	}

	public void getSubjects() {
		Iterator<TypedDependency> i = tdl.iterator();

		while (i.hasNext()) {
			subject = i.next();

			String s = subject.reln().toString();
			if (s.contains("nsubj")) {
				subjects.add(subject);
			}
		}
	}

	// Get dependency SUBJECT
	public void readSubject() {

		Iterator<TypedDependency> i = tdl.iterator();

		
		System.out.println(tdl +"\n");
		while (i.hasNext()) {
			subject = i.next();

			String s = subject.reln().toString();
			if (s.contains("nsubj")) {
				// System.out.println(" " + subject.reln() + "  "
				// + subject.gov().value() + " " + subject.gov().index()
				// + subject.dep().value() + " " + subject.dep().index());

				String firstInSubjectDependency = subject.gov().value();
				String secondInSubjectDependency = subject.dep().value();
				getNextDependency(firstInSubjectDependency);

				getNextDependency(secondInSubjectDependency);

				checkNestedDependencies();

				clauses.add(subject);

				getSimpleSentence();

				clauses.clear();

			}
		}

		System.out.println(simpleSentences);
		System.out.println("________________");
	}

	

	// Get dependency linked to the clause Subject
	public void getNextDependency(String sub) {
		Iterator<TypedDependency> i = tdl.iterator();

		while (i.hasNext()) {
			t = i.next();
			
			String s = t.reln().toString();

			// Clauses not same as Subjects not to be added
			AsSubjectPresent();
			
			if (present)
				present = false;
			else if ((!s.contains("nsubj"))
					&& ((t.gov().value().equals(sub)) || (t.dep().value()
							.equals(sub)))) {
				// Unique clauses

				Iterator<TypedDependency> cl = clauses.iterator();

				while (cl.hasNext()) {
					t1 = cl.next();
					if ((t1.reln().toString().equals(t.reln().toString()))
							&& (t1.gov().value().equals(t.gov().value()))
							&& (t1.dep().value().equals(t.dep().value()))) {
						// System.out.println("DO NOT ADD");
						present = true;
						break;
					} else
						present = false;
				}

				if (!present)
					clauses.add(t);
				else
					present = false;

			}
		}

	}

	public void checkNestedDependencies() {
		Iterator<TypedDependency> i = clauses.iterator();

		while (i.hasNext()) {
			t1 = i.next();

			String s1 = t1.gov().value();
			String s2 = t1.dep().value();
			getNextDependency(s1);

			getNextDependency(s2);

		}

	}
	public void AsSubjectPresent() {
		Iterator<TypedDependency> subj = subjects.iterator();

		while (subj.hasNext()) {
			t1 = subj.next();

			if ((!(t.reln().toString().contains("nsubj")))
					&& (((t1.gov().value().equals(t.gov().value()))) && ((t1
							.dep().value().equals(t.dep().value()))))
					|| (((t1.gov().value().equals(t.dep().value()))) && ((t1
							.dep().value().equals(t.gov().value()))))) {

				present = true;
				break;
			} else
				present = false;
		}

	}

	
	public int ruchir() {
		Iterator<TypedDependency> subj = subjects.iterator();

		while (subj.hasNext()) {
			t1 = subj.next();

			if ((!(t.reln().toString().contains("nsubj")))
					&& (((t1.gov().value().equals(t.gov().value()))) || ((t1
							.dep().value().equals(t.dep().value())))
					|| ((t1.gov().value().equals(t.dep().value()))) || ((t1
							.dep().value().equals(t.gov().value()))))) {

			return 0;
				
			} else
				return 1;
		}

		
		return 1;
	}

	public void getSimpleSentence() {
		Map<Integer, String> words = new HashMap<Integer, String>();

		Iterator<TypedDependency> c1 = clauses.iterator();

		while (c1.hasNext()) {
			t1 = c1.next();

			if (!words.containsKey(t1.gov().index()) && (t1.gov().index() != 0))
				words.put(t1.gov().index(), t1.gov().value());

			if (!words.containsKey(t1.dep().index()))
				words.put(t1.dep().index(), t1.dep().value());

		}

		List<Integer> sortedKeys = new ArrayList<Integer>(words.keySet());
		Collections.sort(sortedKeys);

		Iterator<Integer> i = sortedKeys.iterator();
		while (i.hasNext()) {
			int n = (int) i.next();

			w = w.concat(words.get(n) + " ");
		}

		simpleSentences.add(w);
		w = "";

	}

	public static void main(String[] args) throws IOException {

		TryStanfordParser o = new TryStanfordParser();

	}
}
