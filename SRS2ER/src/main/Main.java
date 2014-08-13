/*
 * This file marks the start of execution of the program.
 * SRS2ER - Program to extract conceptual schema from requirement definition document.
 */

package main;

import java.io.IOException;

import read_tagged_sentences.ReadTaggedTestDataXml;
import read_test_data.ReadParagraph;
import read_training_data_xml.ReadXML;

/**
 * Driver routines to begin execution of the program
 * @author Pooja Mantri (Edited by Suresh Sarda)
 */

public class Main {
	public static void main(String[] args) throws IOException {
		ReadXML r = new ReadXML();
		r.XMLReader();
		r.read();// Read Training Data XML file
		// r.display();

		
		
		System.out.println("********TESTING********");
		
		
		System.out.println();
		new ReadParagraph();
		
		new ReadTaggedTestDataXml();

	}
}