/*
 * This file marks the start of execution of the program.
 * SRS2ER - Program to extract conceptual schema from requirement definition document.
 */

package main;

import insert_into_trie.InsertIntoLeafNode;

import java.io.IOException;

import read_tagged_sentences.ReadTaggedTestDataXml;
import read_test_data.ReadParagraph;
import read_training_data_xml.ReadXML;

/**
 * Driver routines to begin execution of the program
 * 
 * @author Pooja Mantri (Edited by Suresh Sarda)
 */

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("\n\n TRAINING  ");

		ReadXML r = new ReadXML();
		r.XMLReader();
		r.read();// Read Training Data XML file
		// r.display();
System.out.println();
		new InsertIntoLeafNode();
		System.out.println("\n\n TESTING Tagging Paragraph.. ");

		System.out.println();

		new ReadParagraph();

		System.out.println("\n\n\n PROCESSING TAGGED TEST SENTENCE \n");

		new ReadTaggedTestDataXml();
		

	}
}