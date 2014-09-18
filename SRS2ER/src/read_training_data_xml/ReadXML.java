package read_training_data_xml;

import insert_into_trie.InsertIntoTrie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import node.Attribute;
import node.Entity;
import node.Relationship;
import node.Sequence;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import stopwords.RemoveStopWords;

/**
 * Read Training Data (in XML format)
 * 
 * @author Pooja Mantri
 */
public class ReadXML {

	// List of Training Sequences is stored
	public static List<Sequence> Sequences = new ArrayList<Sequence>();
	Sequence s;
	int token_id, ent_name, att_name, rel_name;
	String pos_tag;
	Entity e;
	Relationship r;

	public Document xmlDocument;

	// XML reader to read XML document
	public void XMLReader() {

		try {

			String trainingData = "output.xml";
			// Training Data XML file
			FileInputStream file = new FileInputStream(new File(trainingData));

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();

			DocumentBuilder builder = builderFactory.newDocumentBuilder();

			xmlDocument = builder.parse(file);

		} catch (FileNotFoundException e) {
			System.err.println("Training Data File Not Found");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	// Read all training sentences from XML file
	public void read() {
		InsertIntoTrie t = new InsertIntoTrie();
		XPath xPath = XPathFactory.newInstance().newXPath();

		String expression = "/Sentences/Sentence";

		Node node;
		try {

			// Node List of sentence
			NodeList sentList = (NodeList) xPath.compile(expression).evaluate(
					xmlDocument, XPathConstants.NODESET);
			NodeList nodeList;

			// Until training Sentence is available
			for (int z = 0; sentList != null && z < sentList.getLength(); z++) {

				// Next Training Sentence
				node = sentList.item(z);

				// Create a new Sequence object to store info
				Sequences.add(new Sequence());

				// Child nodes Id,Value,PartOfSpeech & DataModeling
				nodeList = node.getChildNodes();

				for (int j = 0; nodeList != null && j < nodeList.getLength(); j++) {

					Node nod = nodeList.item(j);

					// if child node is value
					if (nod.getNodeName().equals("Value")) {
						// last newly inserted sequence object
						s = Sequences.get(Sequences.size() - 1);

						// Set english sentence in sequence object
						s.sentence = nod.getFirstChild().getNodeValue();

					}
					// Child nodes like Entities,relationships
					NodeList nodeL = nod.getChildNodes();

					// Store Entities and Relationships info in Sequence object
					storeData(nodeL);

				}// Stored all training sentences

				try {
					// Remove Stop words and corresponding POS tags
					new RemoveStopWords(0);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Insert the sequence in the trie
				t.traverseTrie();
			}

		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}

	}

	// Store POS tags and Token ID from XML into sequence object
	public void storeData(NodeList nodeL) {
		for (int i = 0; nodeL != null && i < nodeL.getLength(); i++) {

			// Words,Entities,Relationships
			Node n = nodeL.item(i);

			// Token,Value,Entity,Relation
			NodeList nodeLL = n.getChildNodes();

			for (int k = 0; nodeLL != null && k < nodeLL.getLength(); k++) {

				// Token,Entity,Relation
				Node n2 = nodeLL.item(k);
				// If parent node is Token
				if (n2.getParentNode().getNodeName().equals("Words")) {
					storePOS(n2);

				}
				// If parent node is Entities
				else if (n2.getParentNode().getNodeName().equals("Entities")) {
					// Store Entity and Attribute in sequence
					storeEntities(n2);
				}
				// If parent node is Relationships
				else if (n2.getParentNode().getNodeName()
						.equals("Relationships")) {
					// Store Relationship and entities in sequence
					storeRelationships(n2);
				}
			}

		}
	}

	// Store POS sequence
	public void storePOS(Node n) {

		// WordId,Value,Entity,Relation
		NodeList nodeLL = n.getChildNodes();

		for (int k = 0; nodeLL != null && k < nodeLL.getLength(); k++) {

			// WordId,Value,Entity,Relation
			Node n2 = nodeLL.item(k);

			// If node is WordId
			if (n2.getNodeName().equals("WordId")) {
				// Get token ID

				token_id = Integer.valueOf(n2.getFirstChild().getNodeValue());
				// Next Sibling(#text)
				k = k + 2;

				// Node Value
				n2 = nodeLL.item(k);

				// Get POS tag
				pos_tag = n2.getFirstChild().getNodeValue();

				// Store token_id and POS tag pair in the Map
				s.pos.put(token_id, pos_tag);
			}
		}

	}

	// Store Entity and Attribute in sequence
	public void storeEntities(Node n) {
		// WordId,Attribute
		NodeList nodeLt = n.getChildNodes();

		for (int m = 0; nodeLt != null && m < nodeLt.getLength(); m++) {

			Node n5 = nodeLt.item(m);

			// If node is WordId
			if (n5.getNodeName().equals("WordId")) {
				// Get token ID
				String id = n5.getFirstChild().getNodeValue();
				// Next Sibling(#text)
				m = m + 2;

				// Node Value
				n5 = nodeLt.item(m);

				// Get POS tag
				String nm = n5.getFirstChild().getNodeValue();

				// Add a new entity object with it's token_id in the sequence
				// object
				s.Entities.add(new Entity(id, nm));
			}

			// If node is Attribute
			else if (n5.getNodeName().equals("Attribute")) {

				// WordId,Attribute
				NodeList nodeAtr = n5.getChildNodes();

				for (int p = 0; nodeAtr != null && p < nodeAtr.getLength(); p++) {

					Node n6 = nodeAtr.item(p);

					// If node is WordId
					if (n6.getNodeName().equals("WordId")) {
						// Get token ID
						String id1 = n6.getFirstChild().getNodeValue();
						// Next Sibling(#text)
						p = p + 2;

						// Node Value
						n6 = nodeAtr.item(p);

						// Get POS tag
						String nm1 = n6.getFirstChild().getNodeValue();

						// Get last inserted entity object
						e = s.Entities.get(s.Entities.size() - 1);

						// Add attribute object with it's token_id in sequence
						// object
						e.Attr.add(new Attribute(id1, nm1));
					}

				}
			}
		}
	}

	// Store Relationship and entities in sequence
	public void storeRelationships(Node n) {
		// WordId,Connects
		NodeList nodeLt = n.getChildNodes();

		for (int m = 0; nodeLt != null && m < nodeLt.getLength(); m++) {

			Node n6 = nodeLt.item(m);

			// If node is WordId
			if (n6.getNodeName().equals("WordId")) {
				// Get token ID
				String id = n6.getFirstChild().getNodeValue();
				// Next Sibling(#text)
				m = m + 2;

				// Node Value
				n6 = nodeLt.item(m);

				// Get POS tag
				String nm = n6.getFirstChild().getNodeValue();

				// Add a new entity object with it's token_id in the sequence
				// object
				s.RelationShips.add(new Relationship(id, nm));
			}
			// If node is Connects
			else if (n6.getNodeName().equals("Connects")) {
				// WordId
				NodeList l = n6.getChildNodes();
				// Get last inserted relationship object
				r = s.RelationShips.get(s.RelationShips.size() - 1);

				for (int m2 = 0; l != null && m2 < l.getLength(); m2++) {

					Node n8 = l.item(m2);
					NodeList l2 = n8.getChildNodes();

					for (int q = 0; l2 != null && q < l2.getLength(); q++) {
						// WordId
						Node n7 = l2.item(q);

						if (n7.getNodeType() == Node.ELEMENT_NODE) {

							// Get Node value i.e. WordId
							String b = n7.getFirstChild().getNodeValue();

							// Check from list of entity objects
							for (int v = 0; v < s.Entities.size(); v++) {

								// If entity_name from list = word_id node value
								if (s.Entities.get(v).entity_word_id.equals(b)) {

									// Add corresponding entity object in the
									// relationships
									r.Ent.add(s.Entities.get(v));
								}
							}

						}

					}
				}
			}
		}
	}

	// Display Sequence of training data
	public void display() {

		for (int i = 0; i < Sequences.size(); i++) {
			s = Sequences.get(i);
			System.out.println(s.sentence);

			System.out.println("  POS sequence: " + s.pos);
			System.out.println("Entity :" + s.Entities.get(0).entity_name);
			if (!(s.RelationShips.isEmpty()))
				System.out.println(" Relationship "
						+ s.RelationShips.get(0).relationship_name);
		}

	}

}