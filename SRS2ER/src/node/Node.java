package node;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Node {

	//Leaf  node contains complete information from XML
	public Map<Sequence,Integer> LeafNode=new ConcurrentHashMap<Sequence,Integer>();
	
	// POS tag and next node i.e. Edge
	public Map<String, Node> edges = new HashMap<String, Node>();
}
