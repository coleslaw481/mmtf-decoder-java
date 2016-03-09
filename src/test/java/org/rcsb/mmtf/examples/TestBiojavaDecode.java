package org.rcsb.mmtf.examples;

import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.LocalPDBDirectory.FetchBehavior;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
import org.junit.Test;
import org.rcsb.mmtf.decoder.CheckOnBiojava;
import org.rcsb.mmtf.decoder.CheckOnRawApi;
import org.rcsb.mmtf.decoder.ParsingParams;

public class TestBiojavaDecode {

  private HandleIO handleIo;
  private ParseUsingBioJava parseUsingBioJava;
  private AtomCache cache;
  private FileParsingParameters params;
  private CheckOnBiojava checkEquiv;

  public TestBiojavaDecode() {
    // Get the class to parse anf get data
    parseUsingBioJava = new ParseUsingBioJava();
    handleIo = new HandleIO();
    // Set the cache and the parameters
    cache = new AtomCache();
    cache.setUseMmCif(true);
    cache.setFetchBehavior(FetchBehavior.FETCH_FILES);
    params = cache.getFileParsingParams();
    params.setCreateAtomBonds(true);
    params.setAlignSeqRes(true);
    params.setParseBioAssembly(true);
    DownloadChemCompProvider dcc = new DownloadChemCompProvider();
    ChemCompGroupFactory.setChemCompProvider(dcc);
    dcc.setDownloadAll(true);
    dcc.checkDoFirstInstall();
    params.setUseInternalChainId(true);
    checkEquiv = new CheckOnBiojava();
    StructureIO.setAtomCache(cache);
  }

  /**
   * Basic test to go through a series of PDBs and make sure they are the same
   */
  @Test
  public void basicParsingTest() {
    // Test it for a series of structures
    testParsing("4cup");
  }

  
  private void testParsing(String inputPdb) { 
    System.out.println("TESTING: "+inputPdb);
    byte[] inputByteArr = handleIo.getFromUrl(inputPdb);
    ParsingParams parsingParms = new ParsingParams();
    parsingParms.setParseInternal(params.isUseInternalChainId());
    Structure mmtfStruct = parseUsingBioJava.getBiojavaStruct(inputByteArr, parsingParms);
    // Now parse from the MMCIF file
    Structure mmcifStruct;
    try {
      mmcifStruct = StructureIO.getStructure(inputPdb);
    } catch (IOException e) {
      // Error accessing mmcif
      System.err.println("Error accessing MMCIF");
      e.printStackTrace();
      throw new RuntimeException();
    } catch (StructureException e) {
      System.err.println("Error parsing/consuming MMCIF");
      e.printStackTrace();
      throw new RuntimeException();
    }
    checkEquiv.checkIfStructuresSame(mmtfStruct, mmcifStruct);
    // Now do the checks on the Raw data
    CheckOnRawApi checkRaw = new CheckOnRawApi(inputByteArr);
    checkRaw.checkRawDataConsistency(mmcifStruct, params);

  }
}
