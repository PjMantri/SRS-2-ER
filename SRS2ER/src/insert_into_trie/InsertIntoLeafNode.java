package insert_into_trie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import node.Attribute;
import node.Entity;
import node.Node;
import node.Relationship;
import node.Sequence;

public class InsertIntoLeafNode {
	static int count = 0, total = 0;
	Sequence s, seq;
	int a;
	Iterator<Entity> etCurrent;
	Iterator<Entity> etPrevious;

	Iterator<Attribute> atCurrent;
	Iterator<Attribute> atPrevious;

	Entity current, previous;
	Attribute atCurr, atPrev;

	Iterator<Relationship> rtCurrent;
	Iterator<Relationship> rtPrevious;

	Iterator<Entity> etrCurrent;
	Iterator<Entity> etrPrevious;

	Relationship rcurrent, rprevious;
	Entity erCurr, erPrev;

	Boolean entityDifferent = false, attributeDifferent = false;

	Boolean relationshipDifferent = false, entDifferent = false;
	
	public static List<Node> analyze=new ArrayList<Node>();
	
	public InsertIntoLeafNode()
	{
		System.out.println("\nTotal trained sentences: "+total);
		System.out.println("Sentences with Same POS diferent ER : "+analyze.size()+"\n");
		for(Node n:analyze)
			display(n);
	}

	// Insert into leaf node ER tags and relationship
	public InsertIntoLeafNode(Node n, Sequence s1) {
		++total;
		//System.out.println("Trained " + total + " sentences");
		seq = s1;
		etCurrent = seq.Entities.iterator();
		rtCurrent = seq.RelationShips.iterator();

		if (n.LeafNode.size() > 0) {
			// System.out.println("There is a similar pos sequence ");
			Set<Sequence> seqPresent = n.LeafNode.keySet();

			Iterator<Sequence> it = seqPresent.iterator();

			// Sequence[] Seq=(Sequence[]) seqPresent.toArray();
			// for(Sequence s:Seq){
			while (it.hasNext()) {
				s = it.next();
				etPrevious = s.Entities.iterator();
				rtPrevious = s.RelationShips.iterator();

				List<String> en1 = new ArrayList<String>();
				while (etCurrent.hasNext()) {
					current = etCurrent.next();
					en1.add(current.entity_word_id);
				}

				List<String> en2 = new ArrayList<String>();
				while (etPrevious.hasNext()) {
					previous = etPrevious.next();
					en2.add(previous.entity_word_id);
				}

				if (en1.containsAll(en2)) {

					atPrevious = previous.Attr.iterator();
					atCurrent = current.Attr.iterator();

					List<String> at1 = new ArrayList<String>();
					while (atCurrent.hasNext()) {
						atCurr = atCurrent.next();
						at1.add(atCurr.attribute_word_id);
					}

					List<String> at2 = new ArrayList<String>();
					while (atPrevious.hasNext()) {
						atPrev = atPrevious.next();
						at2.add(atPrev.attribute_word_id);
					}

					if (!(at1.containsAll(at2))) {
						// First Attribute name is different
						attributeDifferent = true;

					}

				} else
				// First Entity name is different
				{
					entityDifferent = true;

				}

				// If entity name or attribute is different
				if (entityDifferent || attributeDifferent) {

					// Same POS different ER create new pair
					n.LeafNode.put(seq, 1);

					count++;
					System.out.println("Entity or attribute Different :"
							+ count);
					
					if(!(analyze.contains(n)))
						analyze.add(n);
					
					//display(n);
					// Reset values
					entityDifferent = false;
					attributeDifferent = false;
					// break;
				} else// Check relationship
				{
					List<String> r1 = new ArrayList<String>();
					while (rtCurrent.hasNext()) {
						rcurrent = rtCurrent.next();
						r1.add(rcurrent.relationship_word_id);
					}

					List<String> r2 = new ArrayList<String>();
					while (rtPrevious.hasNext()) {
						rprevious = rtPrevious.next();
						r2.add(rprevious.relationship_word_id);
					}

					if ((r1.containsAll(r2)) && !(r1.isEmpty()) && !(r2.isEmpty())) {
						etrPrevious = rprevious.Ent.iterator();
						etrCurrent = rcurrent.Ent.iterator();

						List<String> e1 = new ArrayList<String>();
						while (etrCurrent.hasNext()) {
							erCurr = etrCurrent.next();
							e1.add(erCurr.entity_word_id);
						}

						List<String> e2 = new ArrayList<String>();
						while (etrPrevious.hasNext()) {
							erPrev = etrPrevious.next();
							e2.add(erPrev.entity_word_id);
						}

						if (!(e1.containsAll(e2))) {

							entDifferent = true;

						}

					} else if(!(r1.containsAll(r2)))
					// First Relationship name is different
					{
						relationshipDifferent = true;

					}

					// if all ER tagging is same increment frequency
					if (!(entDifferent) && !(relationshipDifferent)) {
						// System.out
						// .println("Increment frequency same pos same er ");
						// System.out.println(n.LeafNode.containsKey(s));
						int h = n.LeafNode.get(s);
						n.LeafNode.remove(s);
						n.LeafNode.put(seq, (h + 1));

						// System.out.println(n.LeafNode);
					}

					if (entDifferent || relationshipDifferent) {
						count++;
						System.out
								.println(" Relationship Different : "
										+ count);
						
						
						
						if(!(analyze.contains(n)))
							analyze.add(n);
						//display(n);
					}
				}
			}

		} else {

			// System.out.println("First occurance");

			// Same POS Sequence or ER tag not found earlier put frequency 0
			n.LeafNode.put(seq, 1);

		}

	}

	public void display(Node n) {

		Set<Sequence> Seq = n.LeafNode.keySet();
		Sequence s;
		Iterator<Sequence> i = Seq.iterator();
		while (i.hasNext()) {
			s = i.next();
			
			System.out.println("\n\nEnglish : " + s.sentence);
			System.out.println("POS sequence :" + s.pos+"\n");

			List<Entity> e = s.Entities;
			for (Entity e1 : e) {
				System.out.println("Entity: " + e1.entity_name);

				if (!e1.Attr.isEmpty()) {
					List<Attribute> a = e1.Attr;
					System.out.println("Attributes :");
					for (Attribute a1 : a) {
						System.out.println(a1.attribute_name);
					}
				}
			}

			List<Relationship> r = s.RelationShips;
			for (Relationship r1 : r) {
				System.out.println("\nRelationship: " + r1.relationship_name);

				List<Entity> c = r1.Ent;
				System.out.println("\nConnects :");
				for (Entity c1 : c) {
					System.out.println(c1.entity_name);
				}

			}
		}
		
		
		System.out.println("_______________________________________");
	}
}
