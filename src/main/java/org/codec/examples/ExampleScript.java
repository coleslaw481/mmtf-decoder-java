package org.codec.examples;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.biojava.nbio.structure.Structure;

public class ExampleScript {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// Get the class to get biojavastructs
		
		GetBioJavaStructs gbjs = new GetBioJavaStructs();
		Structure thisStruct = gbjs.getFromFileSystem("/Users/anthony/codec-devel/data/structures", "4cup");
		Structure thatStruct = gbjs.getFromUrl("4cup");
	}

}
