package read_test_data;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import insert_into_trie.*;
import node.*;

public class ERTagTestData {

	// Root   node to traverse
	Node n = InsertIntoTrie.rootNode;

	String[] engSentence = new String[30];
	String[] posTag = new String[30];

	int[] ind = new int[30];
	String sentence;
	static int correctTag = 0;
	static int totalTestSentences = 0;
	Boolean reachedLeaf = true;
	int indx = 0;

	Iterator<Integer> in;

	Iterator<String> p;

	public ERTagTestData(String eng, Collection<String> pos, Set<Integer> index)
			throws IOException {

		++totalTestSentences;
		sentence = eng;
		engSentence = eng.split(" ");

		p = pos.iterator();
		in = index.iterator();

		while (p.hasNext()) {
			posTag[indx] = p.next();
			ind[indx++] = in.next();
		}

		for (int i = 0; i < indx; i++) {

			// Trace the Trie till node contains pos tag
			if (n.edges.containsKey(posTag[i])) {

				//System.out.println("Contains " + posTag[i]);
				n = n.edges.get(posTag[i]);

				// System.out.println("-------------- "+n.edges);

			} else // tags not found in trie
			{
				//System.out.println("\nTRIE DOESNT CONTAIN " + posTag[i]);

				// p.fw.writeChars("NOT PRESENT IS  " + eng);

				System.out.println("NOT PRESENT IS  " + eng);
				reachedLeaf = false;

				break;

			}
		}

		if (reachedLeaf) {
			// Found in the trie
			fetchLeafNode();
		}

	}

	public void fetchLeafNode() {

		++correctTag;

		Set<Entry<Sequence, Integer>> output = n.LeafNode.entrySet();

		Iterator<Entry<Sequence, Integer>> i = output.iterator();

		while (i.hasNext()) {
			Entry<Sequence, Integer> e = i.next();

			Sequence s = e.getKey();
			int v = e.getValue();
			System.out.println("\n\nFrequency is " + v);
			System.out.println("English sentence is " + sentence);
			System.out.println("POS sequence after removing stop words "
					+ s.pos);
			List<Entity> ent = s.Entities;

			Iterator<Entity> en = ent.iterator();

			while (en.hasNext()) {
				Entity ee = en.next();
				String[] entityId = ee.entity_word_id.split(";");

				int ename,elen,aname,alen;
				
				ename=Integer.valueOf(entityId[0]);
				elen=Integer.valueOf(entityId[1]);
				System.out.println("\nEntity :");
				
				for(int k=ename;k<(ename+elen);k++)
				System.out.print( engSentence[k]);
				System.out.println("\n\nAttribute are ");
				List<Attribute> aa = ee.Attr;

				Iterator<Attribute> ai = aa.iterator();

				while (ai.hasNext()) {

					Attribute a = ai.next();
					String[] attrId = a.attribute_word_id.split(";");
					aname=Integer.valueOf(attrId[0]);
					alen=Integer.valueOf(attrId[1]);
					
					for(int k=aname;k<(aname+alen);k++)
					System.out.println( engSentence[k]);
				}
			}
/*
			List<Relationship> rel = s.RelationShips;

			Iterator<Relationship> relation = rel.iterator();

			while (relation.hasNext()) {
				Relationship r = relation.next();
				int rname = r.relationship_name;

				List<Entity> c = r.Ent;
				Iterator<Entity> conn = c.iterator();

				System.out.println("Relationship is : " + engSentence[rname]);

				while (conn.hasNext()) {
					Entity a = conn.next();
					int ec = a.entity_name;
					System.out.println("Connects " + engSentence[ec]);
				}

			}
*/
		}

		reachedLeaf = false;
	}

}