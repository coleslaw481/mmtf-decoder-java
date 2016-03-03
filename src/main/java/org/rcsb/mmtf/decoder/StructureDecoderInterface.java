package org.rcsb.mmtf.decoder;

import java.util.List;
import java.util.Map;

/**
 * Interface to inflate a given mmtf data source.
 *
 * @author Anthony Bradley
 */
public interface StructureDecoderInterface {

  /**
   * Sets the number of models.
   * @param modelCount Number of models
   */
  void setModelCount(int modelCount);

  /**
   * Sets the number of chains for a given model.
   * @param modelId Identifier of the model within the structure
   * @param chainCount Total number of chains within this model
   */
  void setModelInfo(int modelId, int chainCount);

  /**
   * Sets the information for a given chain.
   * @param chainId chain identifier - length of one to four
   * @param groupCount number of groups/residues in chain
   */
  void setChainInfo(String chainId, int groupCount);

  /**
   * Updates the information for this chain.
   *
   * @param chainId the chain id
   * @param groupCount the group count
   */
  void updateChainInfo(String chainId, int groupCount);


  /**
   * Sets the information for a given group.
   * @param groupName 3 letter code name of this group/residue
   * @param groupNumber sequence position of this group
   * @param insertionCode the one letter insertion code
   * @param polymerType 0 is amino acid, 1 is nucleotide, 2 is hetero group
   * @param atomCount the number of atoms in the group
   */
  void setGroupInfo(String groupName, int groupNumber, char insertionCode,
      int polymerType, int atomCount);

  /**
   * Sets the atom level information for a given atom.
   * @param atomName 1-3 long string of the unique name of the atom
   * @param serialNumber a number counting atoms in a structure
   * @param alternativeLocationId a character indicating the alternate
   * location of the atom
   * @param x the x cartesian coordinate
   * @param y the y cartesian coordinate
   * @param z the z cartesian coordinate
   * @param occupancy the atomic occupancy
   * @param temperatureFactor the B factor (temperature factor)
   * @param element a 1-3 long string indicating the element of the atom
   * @param charge the atomic charge
   */
  void setAtomInfo(String atomName, int serialNumber, char alternativeLocationId, 
      float x, float y, float z, float occupancy, float temperatureFactor, String element, int charge);


  /**
   * Sets the Bioassembly information for the structure.
   *
   * @param keyList the key list
   * @param sizeList the size list
   * @param inputIds the input ids
   * @param inputChainIds the input chain ids
   * @param inputTransformations the input transformations
   */
  void setBioAssembly(Map<Integer, Integer> keyList, Map<Integer,
      Integer> sizeList, Map<Integer, List<String>> inputIds, Map<Integer,
      List<String>> inputChainIds, Map<Integer, List<double[]>>
  inputTransformations);

  /**
   * Sets the space group and unit cell information.
   *
   * @param spaceGroup the space group
   * @param list the list
   */
  void setXtalInfo(String spaceGroup, List<Float> list);

  /**
   * Sets the intra bonds for groups.
   *
   * @param thisBondIndOne the this bond ind one
   * @param thisBondIndTwo the this bond ind two
   * @param thisBondOrder the this bond order
   */
  void setGroupBonds(int thisBondIndOne, int thisBondIndTwo, int thisBondOrder);

  /**
   * Sets the inter group bonds between atoms.
   *
   * @param thisBondIndOne the this bond ind one
   * @param thisBondIndTwo the this bond ind two
   * @param thisBondOrder the this bond order
   */
  void setInterGroupBonds(int thisBondIndOne,
      int thisBondIndTwo, int thisBondOrder);
}
