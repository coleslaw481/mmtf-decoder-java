package org.rcsb.mmtf.decoder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.arraydecompressors.DeltaDeCompress;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeInt;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeString;
import org.rcsb.mmtf.arraydecompressors.RunLengthDelta;
import org.rcsb.mmtf.dataholders.BioAssemblyInfoNew;
import org.rcsb.mmtf.dataholders.BiologicalAssemblyTransformationNew;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Decode an MMTF structure using a structure inflator.
 * @author Anthony Bradley
 *
 */
public class DecodeStructure {

  /** The number of bytes in an integer. */
  private static final int NUM_BYTES_IN_INT = 4;
  /** The maximum number of chars in a chain entry. */
  private static final int MAX_CHARS_PER_CHAIN_ENTRY = 4;
  /** The number to divide coordinate and b factor values by. */
  private static final float COORD_B_FACTOR_DIVIDER = (float) 1000.0;
  /** The number to divide occupancy values by. */
  private static final float OCCUPANCY_DIVIDER = (float) 100.0;


  /** The struct inflator. */
  private StructureDecoderInterface structInflator;

  /** The cartn x. */
  private int[] cartnX;

  /** The cartn y. */
  private int[] cartnY;

  /** The cartn z. */
  private int[] cartnZ;

  /** The b factor. */
  private int[] bFactor;

  /** The occupancy arr. */
  private int[] occupancyArr;

  /** The atom id. */
  private int[] atomId;

  /** The alt id. */
  private char[] altId;


  /** The ins code. */
  private char[] insCode;

  /** The group num. */
  private int[] groupNum;

  /** The group map. */
  private Map<Integer, PDBGroup> groupMap;

  /** The group list. */
  private int[] groupList;

  /** The last count. */
  private int lastCount;

  /**
   * Function to generate a structure from bytes using a structure inflator.
   *
   * @param myInBytes the my in bytes
   * @param inputStructInflator the input struct inflator
   * @param parsingParams the parsing params
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public final void getStructFromByteArray(final byte[] myInBytes,
      final StructureDecoderInterface inputStructInflator,
      final ParsingParams parsingParams) throws IOException {
    // Set the inflator
    structInflator = inputStructInflator;
    // Create a list of all the nucleic acid ids
    List<String> nucAcidList = new ArrayList<>();
    // Get the decompression
    DeltaDeCompress delta = new DeltaDeCompress();
    RunLengthDelta intRunLengthDelta = new RunLengthDelta();
    RunLengthDecodeInt intRunLength = new RunLengthDecodeInt();
    RunLengthDecodeString stringRunlength = new
        RunLengthDecodeString();
    MmtfBean inputData = new ObjectMapper(new
        MessagePackFactory()).readValue(myInBytes, MmtfBean.class);
    // Get the data
    groupList = bytesToInts(inputData.getGroupTypeList());
    // Read the byte arrays as int arrays
    cartnX = delta.decompressByteArray(inputData.getxCoordBig(),
        inputData.getxCoordSmall());
    cartnY = delta.decompressByteArray(inputData.getyCoordBig(),
        inputData.getyCoordSmall());
    cartnZ = delta.decompressByteArray(inputData.getzCoordBig(),
        inputData.getzCoordSmall());
    bFactor =  delta.decompressByteArray(inputData.getbFactorBig(),
        inputData.getbFactorSmall());
    occupancyArr = intRunLength.decompressByteArray(inputData.getOccList());
    atomId = intRunLengthDelta.decompressByteArray(inputData.getAtomIdList());
    altId = stringRunlength.deCompressStringArrayToChar(
        (ArrayList<String>) inputData.getAltLabelList());
    // Get the insertion code
    insCode = stringRunlength.deCompressStringArrayToChar(
        (ArrayList<String>) inputData.getInsCodeList());
    // Get the groupNumber
    groupNum = intRunLengthDelta.decompressByteArray(
        inputData.getGroupNumList());
    groupMap = inputData.getGroupMap();

    // GET THE MODEL LIST AND THE CHAIN MAP
    byte[] chainList;
    int[] chainsPerModel;
    int[] groupsPerChain;
    if (parsingParams.isParseInternal()) {
      System.out.println("Using asym ids");
      chainList = inputData.getInternalChainList();
    } else {
      System.out.println("Using auth ids");
      chainList = inputData.getChainList();
    }
    chainsPerModel = inputData.getInternalChainsPerModel();
    groupsPerChain = inputData.getInternalGroupsPerChain();
    // Now get the group map
    int modelCounter = -1;
    int groupCounter = 0;
    int chainCounter = 0;
    for (int modelChains: chainsPerModel) {
      modelCounter++;
      structInflator.setModelInfo(modelCounter, modelChains);
      // A list to check if we need to set or update the chains
      HashSet<String> chainIdSet = new HashSet<>();
      int totChainsThisMod = chainCounter + modelChains;
      for (int thisChain = chainCounter;
          thisChain < totChainsThisMod;
          thisChain++) {
        String thisChainIdString = getChainId(chainList, thisChain);
        int groupsThisChain = groupsPerChain[thisChain];
        // If we've already seen this chain -> just update it
        if (chainIdSet.contains(thisChainIdString)) {
          structInflator.updateChainInfo(thisChainIdString, groupsThisChain);
        } else {
          structInflator.setChainInfo(thisChainIdString, groupsThisChain);
          chainIdSet.add(thisChainIdString);
        }
        int nextInd = groupCounter + groupsThisChain;
        for (int thisGroupNum = groupCounter;
            thisGroupNum < nextInd; thisGroupNum++) {
          groupCounter++;
          int atomCount = addGroup(thisGroupNum, nucAcidList);
          lastCount += atomCount;
        }
        chainCounter++;
      }
    }
    // Now set the crystallographic and the bioassembly information
    structInflator.setXtalInfo(inputData.getSpaceGroup(),
        inputData.getUnitCell());
    /// Now get the bioassembly information
    Map<Integer, BioAssemblyInfoNew> bioAss = inputData.getBioAssembly();
    // The maps for storing this information
    Map<Integer, Integer> keyList = new HashMap<Integer, Integer>();
    Map<Integer, Integer> sizeList = new HashMap<Integer, Integer>();
    Map<Integer, List<String>> inputIds = new HashMap<Integer, List<String>>();
    Map<Integer, List<String>> inputChainIds =
        new HashMap<Integer, List<String>>();
    Map<Integer, List<double[]>> inputTransformations =
        new HashMap<Integer, List<double[]>>();
    for (Integer key: bioAss.keySet()) {
      // Get the bioassembly info
      BioAssemblyInfoNew bioAssOld = bioAss.get(key);
      keyList.put(key, bioAssOld.getId());
      sizeList.put(key, bioAssOld.getMacromolecularSize());

      List<String> idList = new ArrayList<String>();
      List<String> chainIdList = new ArrayList<String>();
      List<double[]> transformList = new ArrayList<double[]>();
      for (BiologicalAssemblyTransformationNew
          bioTrans:bioAssOld.getTransforms()) {
        double[] trans = bioTrans.getTransformation();
        String id = bioTrans.getId();
        for (String chainId: bioTrans.getChainId()) {
          idList.add(id);
          chainIdList.add(chainId);
          transformList.add(trans);
        }
      }
      // Now just add these different transforms
      inputIds.put(key, idList);
      inputChainIds.put(key, chainIdList);
      inputTransformations.put(key, transformList);
    }
    // Set the bioassembly information
    structInflator.setBioAssembly(keyList, sizeList,
        inputIds, inputChainIds, inputTransformations);
    // Now add the other bonds between groups
    int[] bondAtomList = bytesToInts(inputData.getBondAtomList());
    int[] bondOrderList = bytesToByteInts(inputData.getBondOrderList());
    for (int i = 0; i < bondOrderList.length; i++) {
      structInflator.setInterGroupBonds(bondAtomList[i * 2],
          bondAtomList[i * 2 + 1], bondOrderList[i]);
    }

  }

  /**
   * Adds the group.
   *
   * @param thisGroupNum the this group num
   * @param nucAcidList the nuc acid list
   * @return the int
   */
  private int addGroup(final int thisGroupNum, final List<String> nucAcidList) {
    // Now get the group
    int g = groupList[thisGroupNum];
    // Get this info
    PDBGroup thisGroup = groupMap.get(g);
    List<String> atomInfo = thisGroup.getAtomInfo();
    int atomCount = atomInfo.size() / 2;
    int thsG = groupNum[thisGroupNum];
    char thsIns = insCode[lastCount];
    // NEEDS FIXING FOR NUCLEIC ACIDS...
    // CURRENTLY JUST CONSIDERS AMINO ACIDS AND HET ATOMS
    // OK SO NOW WE NEED A FLAG TO FIND NUCLEIC ACIDS
    if (nucAcidList.contains(thisGroup.getGroupName())) {
      // Now set this as the group
      structInflator.setGroupInfo(thisGroup.getGroupName(),
          thsG, thsIns, 2, atomCount);
    } else {
      int hetInd;
      if (thisGroup.isHetFlag()) {
        hetInd = 0;
      } else {
        hetInd = 1;
      }
      structInflator.setGroupInfo(thisGroup.getGroupName(), thsG,
          thsIns, hetInd, atomCount);
    }
    // A counter for the atom information
    int atomCounter = 0;
    // Now read the next atoms
    for (int i = lastCount; i < lastCount + atomCount; i++) {
      // Now get all the relevant information here
      String atomName = atomInfo.get(atomCounter * 2 + 1);
      String element = atomInfo.get(atomCounter * 2);
      int charge = thisGroup.getAtomCharges().get(atomCounter);
      int serialNumber = atomId[i];
      char alternativeLocationId = altId[i];
      float x = cartnX[i] / COORD_B_FACTOR_DIVIDER;
      float z = cartnZ[i] / COORD_B_FACTOR_DIVIDER;
      float y = cartnY[i] / COORD_B_FACTOR_DIVIDER;
      float occupancy = occupancyArr[i] / OCCUPANCY_DIVIDER;
      float temperatureFactor = bFactor[i] / OCCUPANCY_DIVIDER;
      structInflator.setAtomInfo(atomName,
          serialNumber, alternativeLocationId,
          x, y, z, occupancy, temperatureFactor, element, charge);
      atomCounter++;
    }
    // Now add the bond information for this group
    List<Integer> bondInds = thisGroup.getBondIndices();
    List<Integer> bondOrders = thisGroup.getBondOrders();
    for (int thisBond = 0; thisBond < bondOrders.size(); thisBond++) {
      int thisBondOrder = bondOrders.get(thisBond);
      int thisBondIndOne = bondInds.get(thisBond * 2);
      int thisBondIndTwo = bondInds.get(thisBond * 2 + 1);
      structInflator.setGroupBonds(thisBondIndOne, thisBondIndTwo,
          thisBondOrder);
    }
    return atomCount;
  }

  /**
   * Function to get the chain id for this chain.
   *
   * @param chainList the chain list
   * @param thisChain the this chain
   * @return the chain id
   */
  public final String getChainId(final byte[] chainList, final int thisChain) {

    int incrementor = 0;
    StringBuilder sb = new StringBuilder();
    byte chainIdOne = chainList[thisChain
                                *
                                MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
    sb.append((char) chainIdOne);
    // Now get the next byte
    incrementor += 1;
    byte chainIdTwo = chainList[thisChain
                                * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
    if (chainIdTwo != (byte) 0) {
      sb.append((char) chainIdTwo);
    }
    incrementor += 1;
    byte chainIdThree = chainList[thisChain
                                  *
                                  MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
    if (chainIdThree != (byte) 0) {
      sb.append((char) chainIdThree);
    }
    incrementor += 1;
    byte chainIdFour = chainList[thisChain
                                 *
                                 MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
    if (chainIdFour != (byte) 0) {
      sb.append((char) chainIdFour);
    }
    return sb.toString();
  }

  /**
   * Function to convert a byte array to an int array.
   *
   * @param inArray the in array
   * @return the int[]
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public final int[] bytesToInts(final byte[] inArray) throws IOException {
    DataInputStream bis = new DataInputStream(new
        ByteArrayInputStream(inArray));
    int numIntsInArr = inArray.length / NUM_BYTES_IN_INT;
    // Define an array to return
    int[] outArr = new int[numIntsInArr];
    for (int i = 0; i < numIntsInArr; i++) {
      outArr[i] = bis.readInt();
    }

    return outArr;
  }

  /**
   * Function to convert a byte array to byte encoded .
   *
   * @param inArray the in array
   * @return the int[]
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public final int[] bytesToByteInts(final byte[] inArray) throws IOException {
    DataInputStream bis = new DataInputStream(new
        ByteArrayInputStream(inArray));
    // Define an array to return
    int[] outArr = new int[inArray.length];
    for (int i = 0; i < inArray.length; i++) {
      outArr[i] = (int) bis.readByte();
    }

    return outArr;

  }


}
