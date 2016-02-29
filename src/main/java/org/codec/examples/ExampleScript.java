package org.codec.examples;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.biojava.nbio.structure.Structure;

/**
 * An example script to run the decoder
 * @author anthony
 *
 */
public class ExampleScript {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// Set the PDB_CACHE_DIR path 
		System.setProperty("PDB_CACHE_DIR","/Users/anthony/PDB_CACHE");
		GetBioJavaStructs gbjs = new GetBioJavaStructs();
		Structure thatStruct = gbjs.getFromUrl("1qmz");
		System.out.println(thatStruct.getChains());
	}

}
