package read_tagged_sentences;

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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import analyze_tagged_sentences.ReviewEntityAndAttributes;

public class ReadTaggedTestDataXml {
	// List of Training   Sequences is stored
	public static List<Tagged> TaggedSequence = new ArrayList<Tagged>();
	Tagged t;
	int ent_name, att_name, rel_name;

	TaggedEntity e;
	TaggedRelationship r;

	public static List<String> EntityNames = new ArrayList<String>();
	public static List<String> AttributeNames = new ArrayList<String>();

	public Document xmlDocument;

	// XML reader to read XML document
	public void XMLReader() {

		try {

			String trainingData = "TaggedTestSentence.xml";
			// Training Data XML file
			FileInputStream file = new FileInputStream(new File(trainingData));

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();

			DocumentBuilder builder = builderFactory.newDocumentBuilder();

			xmlDocument = builder.parse(file);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
				TaggedSequence.add(new Tagged());

				// Child nodes Value,PartOfSpeech & DataModel
				nodeList = node.getChildNodes();

				for (int j = 0; nodeList != null && j < nodeList.getLength(); j++) {

					Node nod = nodeList.item(j);

					// if child node is value
					if (nod.getNodeName().equals("Value")) {
						// last newly inserted sequence object
						t = TaggedSequence.get(TaggedSequence.size() - 1);

						// Set english sentence in sequence object
						t.sentence = nod.getFirstChild().getNodeValue();

					}
					// Child nodes like Entities,relationships
					NodeList nodeL = nod.getChildNodes();

					// Store Entities and Relationships info in Sequence object
					storeData(nodeL);

				}// Stored all tagged test sentences

			}

		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}

	}

	// Store POS tags and Token ID from XML into sequence object
	public void storeData(NodeList nodeL) {
		for (int i = 0; nodeL != null && i < nodeL.getLength(); i++) {

			// Entities,Relationships
			Node n = nodeL.item(i);

			// WordId,Value,Entity,Relation
			NodeList nodeLL = n.getChildNodes();

			for (int k = 0; nodeLL != null && k < nodeLL.getLength(); k++) {

				// WordId,Value,Entity,Relation
				Node n2 = nodeLL.item(k);

				// If parent node is Entities
				if (n2.getParentNode().getNodeName().equals("Entities")) {
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

	// Store Entity and Attribute in sequence
	public void storeEntities(Node n) {
		// WordId,Attribute
		NodeList nodeLt = n.getChildNodes();

		for (int m = 0; nodeLt != null && m < nodeLt.getLength(); m++) {

			Node n5 = nodeLt.item(m);
			// If node is WordId

			if (n5.getNodeName().equals("WordId")) {
				String h = n5.getFirstChild().getNodeValue();

				// Add a new entity object with it's token_id in the sequence
				// object

				t.Entities.add(new TaggedEntity(h));

				if (!EntityNames.contains(h))
					EntityNames.add(h);

			}
			// If node is Attribute
			else if (n5.getNodeName().equals("Attribute")) {
				String h = n5.getFirstChild().getNodeValue();

				// If Entity has an attribute
				if (n5.hasChildNodes()) {
					// Get last inserted entity object
					e = t.Entities.get(t.Entities.size() - 1);

					// Add attribute object with it's token_id in sequence
					// object
					e.Attr.add(new TaggedAttribute(h));

					if (!AttributeNames.contains(h))
						AttributeNames.add(h);

				}

			}
		}
	}

	// Store Relationship and entities in sequence
	public void storeRelationships(Node n) {
		// /WordId,Connects
		NodeList nodeLt = n.getChildNodes();

		for (int m = 0; nodeLt != null && m < nodeLt.getLength(); m++) {

			Node n6 = nodeLt.item(m);

			// If node is WordId
			if (n6.getNodeName().equals("WordId")) {
				// Add relationship object with it's token_id in sequence object
				t.RelationShips.add(new TaggedRelationship(n6.getFirstChild()
						.getNodeValue()));

			}
			// If node is Connects
			else if (n6.getNodeName().equals("Connects")) {
				// WordId
				NodeList l = n6.getChildNodes();
				// Get last inserted relationship object
				r = t.RelationShips.get(t.RelationShips.size() - 1);

				for (int q = 0; l != null && q < l.getLength(); q++) {
					// WordId
					Node n7 = l.item(q);

					if (n7.getNodeType() == Node.ELEMENT_NODE) {

						// Get Node value i.e. WordId
						String b = n7.getFirstChild().getNodeValue();

						// Check from list of entity objects
						for (int v = 0; v < t.Entities.size(); v++) {

							// If entity_name from list = word_id node value
							if (t.Entities.get(v).entity_name == b) {

								// Add corresponding entity object in the
								// relationships
								r.Ent.add(t.Entities.get(v));
							}
						}

					}
				}
			}
		}
	}

	// Display Sequence of training data
	public void display() {

		for (int i = 0; i < TaggedSequence.size(); i++) {
			t = TaggedSequence.get(i);
			System.out.println(t.sentence);

			for (int k = 0; k < t.Entities.size(); k++)
				System.out.println("Entity :" + t.Entities.get(k).entity_name);

			for (int k = 0; k < t.RelationShips.size(); k++)
				System.out.println("Relationship :"
						+ t.RelationShips.get(k).relationship_name);

		}

		
		System.out.println("*************");
		System.out.println("Entities are : ");
		for (int k = 0; k < EntityNames.size(); k++)
			System.out.println( EntityNames.get(k));
		
		
		System.out.println("\nAtttributes are :");
		
		for (int k = 0; k < AttributeNames.size(); k++)
			System.out.println(AttributeNames.get(k));

	}

	public ReadTaggedTestDataXml(){

		XMLReader();
		read();// Read Training Data XML file
		display();
		new ReviewEntityAndAttributes();
		

	}
}
