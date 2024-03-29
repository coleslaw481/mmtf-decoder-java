package org.rcsb.mmtf.decoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.FileParsingParameters;

/**
 * Class to test the raw API
 * @author Anthony Bradley
 *
 */
public class CheckOnRawApi {
  DecodeStructure decodeStructure;
  public CheckOnRawApi(byte[] inputData) {
     decodeStructure = new DecodeStructure(inputData);
  }
  
  /**
   * Test that required data is available the way we would expect.
   */
  public void checkRawDataConsistency(Structure biojavaStruct, FileParsingParameters params) {
    // Series of tests on expected values from the raw API
    assertNotEquals(decodeStructure.getMmtfProducer(), null);
    assertNotEquals(decodeStructure.getMmtfVersion(), null);
    checkIfSeqResInfoSame(biojavaStruct, params);
    // Check other features in the data
      
  }
  
  
  /**
   * Test of sequence and seqres group level information. At the moment the decoder does not parse this data.
   * This test checks to see if the underlying data is how one would expect.
   * @param biojavaStruct
   * @param params
   */
  public void checkIfSeqResInfoSame(Structure biojavaStruct, FileParsingParameters params){
    if(params.isUseInternalChainId()){
      // Get the seqres group list
      int[] decodedSeqResGroupList = decodeStructure.getSeqResGroupList();
      // Get the string sequences
      List<String> sequenceStrings = decodeStructure.getSequenceInfo();
      int groupCounter = 0;
      int chainCounter = 0;
      // Get the sequence information
      for(int currentModelIndex = 0; currentModelIndex < biojavaStruct.nrModels(); currentModelIndex++){
        for(Chain currentChain : biojavaStruct.getChains(currentModelIndex)){
          // Get the sequence
          assertEquals(sequenceStrings.get(chainCounter), currentChain.getSeqResSequence());
          List<Group> thisChainSeqResList = new ArrayList<>();
          for(Group seqResGroup : currentChain.getSeqResGroups()){
            thisChainSeqResList.add(seqResGroup);
          }
          // Now go through and check the indices line up
          for(int i = 0; i < currentChain.getAtomGroups().size(); i++){
            // Get the group
            Group testGroup = currentChain.getAtomGroup(i);
            int testGroupInd = thisChainSeqResList.indexOf(testGroup);
            assertEquals(testGroupInd, decodedSeqResGroupList[groupCounter]);
            groupCounter++;
          }
          chainCounter++;
        }
      }
    }
    // Otherwise we need to parse in a different
    else{
      System.out.println("Using public facing chain ids -> seq res not tested");
    }

  }
}
