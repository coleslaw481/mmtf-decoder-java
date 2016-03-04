package org.rcsb.mmtf.decoder;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Decode an MMTF structure using a structure inflator. The class also allows access to the unconsumed but parsed and inflated underlying data.
 * 
 * @author Anthony Bradley
 *
 */
public class DecodeStructure {

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

  /** The sequence ids of the groups */
  private int[] seqResGroupList;

  /** The internal chain ids*/
  private String[] internalChainIds;

  /** The public facing chain ids*/
  private String[] publicChainIds;

  /** The number of chains per model*/
  private int[] chainsPerModel;

  /** The number of groups per (internal) chain*/
  private int[] groupsPerChain;

  /** The space group of the structure*/
  private String spaceGroup;
  
  /** The unit cell of the structure*/
  private List<Float> unitCell;
  
  /** The bioassembly information for the structure*/
  private Map<Integer, BioAssemblyInfoNew> bioAssembly;
  
  /** The bond indices for bonds between groups*/
  private int[] interGroupBondIndices;
  
  /** The bond orders for bonds between groups*/
  private int[] interGroupBondOrders;
  
  /**
   * @return the spaceGroup
   */
  public String getSpaceGroup() {
    return spaceGroup;
  }

  /**
   * @param spaceGroup the spaceGroup to set
   */
  public void setSpaceGroup(String spaceGroup) {
    this.spaceGroup = spaceGroup;
  }

  /**
   * @return the unitCell
   */
  public List<Float> getUnitCell() {
    return unitCell;
  }

  /**
   * @param unitCell the unitCell to set
   */
  public void setUnitCell(List<Float> unitCell) {
    this.unitCell = unitCell;
  }

  /**
   * @return the bioAssembly
   */
  public Map<Integer, BioAssemblyInfoNew> getBioAssembly() {
    return bioAssembly;
  }

  /**
   * @param bioAssembly the bioAssembly to set
   */
  public void setBioAssembly(Map<Integer, BioAssemblyInfoNew> bioAssembly) {
    this.bioAssembly = bioAssembly;
  }


  /**
   * @return the interGroupBondIndices
   */
  public int[] getInterGroupBondIndices() {
    return interGroupBondIndices;
  }

  /**
   * @param interGroupBondIndices the interGroupBondIndices to set
   */
  public void setInterGroupBondIndices(int[] interGroupBondIndices) {
    this.interGroupBondIndices = interGroupBondIndices;
  }

  /**
   * @return the interGroupBondOrders
   */
  public int[] getInterGroupBondOrders() {
    return interGroupBondOrders;
  }

  /**
   * @param interGroupBondOrders the interGroupBondOrders to set
   */
  public void setInterGroupBondOrders(int[] interGroupBondOrders) {
    this.interGroupBondOrders = interGroupBondOrders;
  }

  /**
   * @return the chainsPerModel
   */
  public int[] getChainsPerModel() {
    return chainsPerModel;
  }

  /**
   * @param chainsPerModel the chainsPerModel to set
   */
  public void setChainsPerModel(int[] chainsPerModel) {
    this.chainsPerModel = chainsPerModel;
  }

  /**
   * @return the groupsPerChain
   */
  public int[] getGroupsPerChain() {
    return groupsPerChain;
  }

  /**
   * @param groupsPerChain the groupsPerChain to set
   */
  public void setGroupsPerChain(int[] groupsPerChain) {
    this.groupsPerChain = groupsPerChain;
  }

  /**
   * @return the cartnX
   */
  public int[] getCartnX() {
    return cartnX;
  }

  /**
   * @param cartnX the cartnX to set
   */
  public void setCartnX(int[] cartnX) {
    this.cartnX = cartnX;
  }

  /**
   * @return the cartnY
   */
  public int[] getCartnY() {
    return cartnY;
  }

  /**
   * @param cartnY the cartnY to set
   */
  public void setCartnY(int[] cartnY) {
    this.cartnY = cartnY;
  }

  /**
   * @return the cartnZ
   */
  public int[] getCartnZ() {
    return cartnZ;
  }

  /**
   * @param cartnZ the cartnZ to set
   */
  public void setCartnZ(int[] cartnZ) {
    this.cartnZ = cartnZ;
  }

  /**
   * @return the bFactor
   */
  public int[] getbFactor() {
    return bFactor;
  }

  /**
   * @param bFactor the bFactor to set
   */
  public void setbFactor(int[] bFactor) {
    this.bFactor = bFactor;
  }

  /**
   * @return the occupancyArr
   */
  public int[] getOccupancyArr() {
    return occupancyArr;
  }

  /**
   * @param occupancyArr the occupancyArr to set
   */
  public void setOccupancyArr(int[] occupancyArr) {
    this.occupancyArr = occupancyArr;
  }

  /**
   * @return the atomId
   */
  public int[] getAtomId() {
    return atomId;
  }

  /**
   * @param atomId the atomId to set
   */
  public void setAtomId(int[] atomId) {
    this.atomId = atomId;
  }

  /**
   * @return the altId
   */
  public char[] getAltId() {
    return altId;
  }

  /**
   * @param altId the altId to set
   */
  public void setAltId(char[] altId) {
    this.altId = altId;
  }

  /**
   * @return the insCode
   */
  public char[] getInsCode() {
    return insCode;
  }

  /**
   * @param insCode the insCode to set
   */
  public void setInsCode(char[] insCode) {
    this.insCode = insCode;
  }

  /**
   * @return the groupNum
   */
  public int[] getGroupNum() {
    return groupNum;
  }

  /**
   * @param groupNum the groupNum to set
   */
  public void setGroupNum(int[] groupNum) {
    this.groupNum = groupNum;
  }

  /**
   * @return the groupMap
   */
  public Map<Integer, PDBGroup> getGroupMap() {
    return groupMap;
  }

  /**
   * @param groupMap the groupMap to set
   */
  public void setGroupMap(Map<Integer, PDBGroup> groupMap) {
    this.groupMap = groupMap;
  }

  /**
   * @return the groupList
   */
  public int[] getGroupList() {
    return groupList;
  }

  /**
   * @param groupList the groupList to set
   */
  public void setGroupList(int[] groupList) {
    this.groupList = groupList;
  }

  /**
   * @return the lastCount
   */
  public int getLastCount() {
    return lastCount;
  }

  /**
   * @param lastCount the lastCount to set
   */
  public void setLastCount(int lastCount) {
    this.lastCount = lastCount;
  }

  /**
   * @return the seqResGroupList
   */
  public int[] getSeqResGroupList() {
    return seqResGroupList;
  }

  /**
   * @param seqResGroupList the seqResGroupList to set
   */
  public void setSeqResGroupList(int[] seqResGroupList) {
    this.seqResGroupList = seqResGroupList;
  }

  /**
   * Make the empty contructor private
   */
  @SuppressWarnings("unused")
  private DecodeStructure(){
    
  }
  
  /**
   * The constructor requires a byte array to fill the data
   * @param inputByteArr
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public DecodeStructure(byte[] inputByteArr) throws JsonParseException, JsonMappingException, IOException{
    // Get the decompression
    DeltaDeCompress deltaDecompress = new DeltaDeCompress();
    RunLengthDelta intRunLengthDelta = new RunLengthDelta();
    RunLengthDecodeInt intRunLength = new RunLengthDecodeInt();
    RunLengthDecodeString stringRunlength = new RunLengthDecodeString();
    DecoderUtils decoderUtils = new DecoderUtils();
    MmtfBean inputData = new ObjectMapper(new
        MessagePackFactory()).readValue(inputByteArr, MmtfBean.class);
    // Get the data
    groupList = decoderUtils.bytesToInts(inputData.getGroupTypeList());
    // Read the byte arrays as int arrays
    cartnX = deltaDecompress.decompressByteArray(inputData.getxCoordBig(),
        inputData.getxCoordSmall());
    cartnY = deltaDecompress.decompressByteArray(inputData.getyCoordBig(),
        inputData.getyCoordSmall());
    cartnZ = deltaDecompress.decompressByteArray(inputData.getzCoordBig(),
        inputData.getzCoordSmall());
    bFactor =  deltaDecompress.decompressByteArray(inputData.getbFactorBig(),
        inputData.getbFactorSmall());
    occupancyArr = intRunLength.decompressByteArray(inputData.getOccList());
    atomId = intRunLengthDelta.decompressByteArray(inputData.getAtomIdList());
    altId = stringRunlength.stringArrayToChar(
        (ArrayList<String>) inputData.getAltLabelList());
    // Get the insertion code
    insCode = stringRunlength.stringArrayToChar(
        (ArrayList<String>) inputData.getInsCodeList());
    // Get the groupNumber
    groupNum = intRunLengthDelta.decompressByteArray(
        inputData.getGroupNumList());
    groupMap = inputData.getGroupMap();
    // Get the seqRes groups
    seqResGroupList = intRunLengthDelta.decompressByteArray(inputData.getSeqResGroupIds());
    // Get the number of chains per model
    chainsPerModel = inputData.getInternalChainsPerModel();
    groupsPerChain = inputData.getInternalGroupsPerChain();
    // Get the internal and public facing chain ids
    publicChainIds = decodeChainList(inputData.getChainList());
    internalChainIds = decodeChainList(inputData.getInternalChainList());
    spaceGroup = inputData.getSpaceGroup();
    unitCell = inputData.getUnitCell();
    bioAssembly  = inputData.getBioAssembly();
    interGroupBondIndices = decoderUtils.bytesToInts(inputData.getBondAtomList());
    interGroupBondOrders = decoderUtils.bytesToByteInts(inputData.getBondOrderList());
    
  }

  /**
   * Decode chain ids from byte arrays
   * @param currentChainList the byte array of the chain list input. Each chain takes up 4 bytes.
   * @return the string array of the parsed chain ids
   */
  private String[] decodeChainList(byte[] currentChainList) {
    DecoderUtils decoderUtils = new DecoderUtils();
    int outputLength = currentChainList.length/4;
    String[] outputArr = new String[outputLength];
    for (int i = 0; i < outputLength; i++){
      outputArr[i] = decoderUtils.getChainId(currentChainList, i);
    }
    return outputArr;
  }
  /**
   * Generate a structure from bytes using a structure inflator.
   *
   * @param myInBytes the my in bytes
   * @param inputStructInflator the input struct inflator
   * @param parsingParams the parsing params
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public final void getStructFromByteArray(final StructureDecoderInterface inputStructInflator,
      final ParsingParams parsingParams) throws IOException {

    // TODO Create a list of all the nucleic acid ids
    List<String> nucAcidList = new ArrayList<>();
    // Set the inflator
    structInflator = inputStructInflator;
    // GET THE MODEL LIST AND THE CHAIN MAP
    String[] chainList;
    if (parsingParams.isParseInternal()) {
      System.out.println("Using asym ids");
      chainList = internalChainIds;
    } else {
      System.out.println("Using auth ids");
      chainList = publicChainIds;
    }
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
      for (int thisChain = chainCounter; thisChain < totChainsThisMod;  thisChain++) {
        String thisChainIdString = chainList[thisChain];
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
    structInflator.setXtalInfo(spaceGroup, unitCell);
    /// Now get the bioassembly information
    Map<Integer, BioAssemblyInfoNew> bioAss = bioAssembly;
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
    for (int i = 0; i < interGroupBondOrders.length; i++) {
      structInflator.setInterGroupBonds(interGroupBondIndices[i * 2],
          interGroupBondIndices[i * 2 + 1], interGroupBondOrders[i]);
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
   * @return the internalChainIds
   */
  public String[] getInternalChainIds() {
    return internalChainIds;
  }

  /**
   * @param internalChainIds the internalChainIds to set
   */
  public void setInternalChainIds(String[] internalChainIds) {
    this.internalChainIds = internalChainIds;
  }

  /**
   * @return the publicChainIds
   */
  public String[] getPublicChainIds() {
    return publicChainIds;
  }

  /**
   * @param publicChainIds the publicChainIds to set
   */
  public void setPublicChainIds(String[] publicChainIds) {
    this.publicChainIds = publicChainIds;
  }


}
