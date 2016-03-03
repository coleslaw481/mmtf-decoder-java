package org.rcsb.mmtf.dataholders;

import javax.vecmath.Point3d;


/**
 * A class to hold c-alpha coords.
 *
 * @author anthony
 */
public class CalphaAlignBean {

  /** The pdb id. */
  private String pdbId;

  /** The chain id. */
  private String chainId;

  /** The polymer type. */
  private String polymerType;

  /** The sequence. */
  private char[] sequence;

  /** The coordinate list. */
  private Point3d[] coordList;


  /**
   * Gets the pdb id.
   *
   * @return the pdb id
   */
  public final String getPdbId() {
    return pdbId;
  }

  /**
   * Sets the pdb id.
   *
   * @param inputPdbId the new pdb id
   */
  public final void setPdbId(final String inputPdbId) {
    this.pdbId = inputPdbId;
  }

  /**
   * Gets the chain id.
   *
   * @return the chain id
   */
  public final String getChainId() {
    return chainId;
  }

  /**
   * Sets the chain id.
   *
   * @param inputChainId the new chain id
   */
  public final void setChainId(final String inputChainId) {
    this.chainId = inputChainId;
  }

  /**
   * Gets the polymer type.
   *
   * @return the polymer type
   */
  public final String getPolymerType() {
    return polymerType;
  }

  /**
   * Sets the polymer type.
   *
   * @param inputPolymerType the new polymer type
   */
  public final void setPolymerType(final String inputPolymerType) {
    this.polymerType = inputPolymerType;
  }

  /**
   * Gets the sequence.
   *
   * @return the sequence
   */
  public final char[] getSequence() {
    return sequence;
  }

  /**
   * Sets the sequence.
   *
   * @param inputSequence the new sequence
   */
  public final void setSequence(final char[] inputSequence) {
    this.sequence = inputSequence;
  }

  /**
   * Gets the coord list.
   *
   * @return the coord list
   */
  public final Point3d[] getCoordList() {
    return coordList;
  }

  /**
   * Sets the coord list.
   *
   * @param inputCoordList the new coord list
   */
  public final void setCoordList(final Point3d[] inputCoordList) {
    this.coordList = inputCoordList;
  }


}
