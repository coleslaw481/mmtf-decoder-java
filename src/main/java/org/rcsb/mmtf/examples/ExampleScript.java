package org.rcsb.mmtf.examples;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.biojava.nbio.structure.Structure;

/**
 * An example script to run the decoder.
 * @author Anthony Bradley
 *
 */
public final class ExampleScript {


  /**
   * Make the constructor private.
   */
  private ExampleScript() {

  }

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws FileNotFoundException the file not found exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void main(final String[] args) throws
  FileNotFoundException, IOException {
    // Set the PDB_CACHE_DIR path
    System.setProperty("PDB_CACHE_DIR", "/Users/anthony/PDB_CACHE");
    GetBioJavaStructs gbjs = new GetBioJavaStructs();
    Structure thatStruct = gbjs.getFromUrl("1qmz");
    System.out.println(thatStruct.getChains());
  }

}
