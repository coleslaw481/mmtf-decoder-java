package org.rcsb.mmtf.dataholders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.biojava.nbio.structure.io.PDBFileParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Group (residue) level data store.
 * @author Anthony Bradley
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDBGroup implements Serializable {

  /** Serial id for this version of the format. */
  private static final long serialVersionUID = 2880633780569899800L;

  /** The group name. (e.g. HIS) */
  private String groupName;

  /** The het flag. Indicates wether the group is a HETATM record or not*/
  private boolean hetFlag;

  /** The atom info. A list of strings indicating
   * the atominfo (Atom name and element name). */
  private List<String> atomInfo;

  /** The bond orders. A list of integers indicating the bond orders*/
  private List<Integer> bondOrders;

  /** The bond indices (in pairs).
   * (e.g. 0,1 means there is bond between atom 0 and 1).*/
  private List<Integer> bondIndices;

  /** The atom charges. */
  // The atomic charges of these atoms
  private List<Integer> atomCharges;

  /** The single letter code. */
  // The single letter code
  private String singleLetterCode;

  /**
   * Constructor for the PDB group. Makes empty lists and sets 
   * hetflag to false by default.
   */
  public PDBGroup(){
    groupName = new String();
    hetFlag = false;
    atomInfo = new ArrayList<String>();
    bondOrders = new ArrayList<Integer>();
    bondIndices = new ArrayList<Integer>();
    atomCharges = new ArrayList<Integer>();
  }
  
  /**
   * Gets the atom info.
   *
   * @return the atom info
   */
  // Generic getter and setter functions
  public final List<String> getAtomInfo() {
    return atomInfo;
  }

  /**
   * Sets the atom info.
   *
   * @param inputAtomInfo the new atom info
   */
  public final void setAtomInfo(final List<String> inputAtomInfo) {
    this.atomInfo = inputAtomInfo;
  }

  /**
   * Gets the bond orders.
   *
   * @return the bond orders
   */
  public final List<Integer> getBondOrders() {
    return bondOrders;
  }

  /**
   * Sets the bond orders.
   *
   * @param inputBondOrders the new bond orders
   */
  public final void setBondOrders(final List<Integer> inputBondOrders) {
    this.bondOrders = inputBondOrders;
  }

  /**
   * Gets the bond indices.
   *
   * @return the bond indices
   */
  public final List<Integer> getBondIndices() {
    return bondIndices;
  }

  /**
   * Sets the bond indices.
   *
   * @param inputBondIndices the new bond indices
   */
  public final void setBondIndices(final List<Integer> inputBondIndices) {
    this.bondIndices = inputBondIndices;
  }

  /**
   * Gets the group name.
   *
   * @return the group name
   */
  public final String getGroupName() {
    return groupName;
  }

  /**
   * Sets the group name.
   *
   * @param resName the new group name
   */
  public final void setGroupName(final String resName) {
    this.groupName = resName;
  }

  /**
   * Checks if is het flag.
   *
   * @return true, if is het flag
   */
  public final boolean isHetFlag() {
    return hetFlag;
  }

  /**
   * Sets the het flag.
   *
   * @param isHetGroup the new het flag
   */
  public final void setHetFlag(final boolean isHetGroup) {
    this.hetFlag = isHetGroup;
  }

  /**
   * Gets the atom charges.
   *
   * @return the atom charges
   */
  public final List<Integer> getAtomCharges() {
    return atomCharges;
  }

  /**
   * Sets the atom charges.
   *
   * @param inputAtomCharges the new atom charges
   */
  public final void setAtomCharges(final List<Integer> inputAtomCharges) {
    this.atomCharges = inputAtomCharges;
  }

  /**
   * Gets the single letter code.
   *
   * @return the single letter code
   */
  public final String getSingleLetterCode() {
    return singleLetterCode;
  }

  /**
   * Sets the single letter code.
   *
   * @param inputSingleLetterCode the new single letter code
   */
  public final void setSingleLetterCode(final String inputSingleLetterCode) {
    this.singleLetterCode = inputSingleLetterCode;
  }
}
