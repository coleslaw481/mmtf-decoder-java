package org.rcsb.mmtf.dataholders;

import javax.vecmath.Point3d;


/**
 * A class to hold c-alpha coords
 * @author anthony
 *
 */
public class CalphaAlignBean {

	private String pdbId;
	private String chainId;
	private String polymerType;
	private char[] sequence;
	private Point3d[] coordList;

	
	public String getPdbId() {
		return pdbId;
	}
	public void setPdbId(String pdbId) {
		this.pdbId = pdbId;
	}
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	public String getPolymerType() {
		return polymerType;
	}
	public void setPolymerType(String polymerType) {
		this.polymerType = polymerType;
	}
	public char[] getSequence() {
		return sequence;
	}
	public void setSequence(char[] sequence) {
		this.sequence = sequence;
	}
	public Point3d[] getCoordList() {
		return coordList;
	}
	public void setCoordList(Point3d[] coordList) {
		this.coordList = coordList;
	}

	
}
