package node;

import java.util.ArrayList;

import java.util.List;

public class Entity {

	// Word Id in English sentence
	public String entity_word_id;
	// Entity name
	public String entity_name;
	// List of Attribute objects
	public List<Attribute> Attr = new ArrayList<Attribute>();

	public Entity(String ent, String nm) {
		entity_word_id = ent;
		entity_name = nm;

	}

}
