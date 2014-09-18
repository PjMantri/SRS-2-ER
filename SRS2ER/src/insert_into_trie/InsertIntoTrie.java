package insert_into_trie;

import java.util.Collection;
import java.util.Iterator;

import read_training_data_xml.ReadXML;
import node.*;

public class InsertIntoTrie {

	// Root node of Trie should be static
	public static Node rootNode = new Node();
	Node nextNode;

	Sequence seq;
	Boolean notFound = false;

	static Boolean root = true;

	String nextPOS;
	Iterator<String> a;

	public InsertIntoTrie()
	{
		
	}
	// Traverse the  trie till POS sequence can be traced
	public void traverseTrie() {
		// create a new node
		nextNode = new Node();

		// start traversing from the root node
		nextNode = rootNode;

		// get last inserted sequence in the list
		seq = ReadXML.Sequences.get(ReadXML.Sequences.size() - 1);

		// Get list of pos tags i.e values in POS map after removing stop
		// words
		Collection<String> ind = seq.pos.values();

		// Iterator over word index and POS tag keypair
		a = ind.iterator();

		// while more POS tag
		while (a.hasNext()) {

			// Next POS tag
			nextPOS = a.next();

			// Trace the Trie till node contains pos tag

			if (nextNode.edges.containsKey(nextPOS)) {
				nextNode = nextNode.edges.get(nextPOS);
			//	System.out.println("Already present ");

			} else // tag not found in trie
			{
				// create a new node to be inserted
				Node temp = new Node();

				//System.out.println("Inserting " + nextPOS);

				// Insert new node in the trie with POS tag not
				// found in the trie and next node for the edge
				nextNode.edges.put(nextPOS, temp);

				// If first node make it the root node
				if (root) {
					rootNode = nextNode;

					// set root as false after inserting root node
					root = false;
				}

				nextNode = temp;
				notFound = true;
				break;
			}

		}

		// If not found insert subsequent tags in the trie
		if (notFound) {
			insertIntoTrie();
		}

		// Insert into leaf node complete info about the sequence
		new InsertIntoLeafNode(nextNode, seq);

	}

	// If not found insert subsequent POS tags in the trie
	public void insertIntoTrie() {

		// insert tags in the trie from that index
		while (a.hasNext()) {

			// Next POS tag
			nextPOS = a.next();

			// create a new node to be inserted
			Node temp = new Node();
		//	System.out.println("Inserting " + nextPOS);

			// Insert new node POS and next node for the edge
			nextNode.edges.put(nextPOS, temp);

			nextNode = temp;

		}

		// reset values
		notFound = false;
		nextPOS = null;
	}

}
