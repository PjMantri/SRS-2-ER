package node;

import java.util.ArrayList;

import java.util.List;

public class Relationship {

	// Word id in English Sentence
	public String relationship_word_id;
	//Relationship name
	public String relationship_name;
	// List of entity objects
	public List<Entity> Ent = new ArrayList<Entity>();

	public Relationship(String id, String nm) {
		relationship_word_id = id;
		relationship_name = nm;

	}

}
