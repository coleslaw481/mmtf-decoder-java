package org.rcsb.mmtf.dataholders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *  Class to store group level information in the MMTF data formate
 * @author anthony
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDBGroup implements Serializable {
	/**
	 * Serial id for this version of the format
	 */
	private static final long serialVersionUID = 2880633780569899800L;
	
	
	// The name of the group (e.g. HIS)
	private String groupName = new String();
	// Whether the group is a HETATM record or not
	private boolean hetFlag = false;
	// A list of strings indicating the atominfo (Atom name and element name
	private List<String> atomInfo = new ArrayList<String>();
	// A list of strings indicating the bond orders
	private List<Integer> bondOrders = new ArrayList<Integer>();
	// A list of strings indicating the bond indices (e.g. 0,1 means there is bond between
	// The 0th and 1st atoms in the group
	private List<Integer> bondIndices = new ArrayList<Integer>();
	// The atomic charges of these atoms
	private List<Integer> atomCharges = new ArrayList<Integer>();
	// The single letter code
	private String singleLetterCode;
	
	// Generic getter and setter functions
	public List<String> getAtomInfo() {
		return atomInfo;
	}
	public void setAtomInfo(List<String> atomInfo) {
		this.atomInfo = atomInfo;
	}
	public List<Integer> getBondOrders() {
		return bondOrders;
	}
	public void setBondOrders(List<Integer> bondOrders) {
		this.bondOrders = bondOrders;
	}
	public List<Integer> getBondIndices() {
		return bondIndices;
	}
	public void setBondIndices(List<Integer> bondIndices) {
		this.bondIndices = bondIndices;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String resName) {
		this.groupName = resName;
	}
	public boolean isHetFlag() {
		return hetFlag;
	}
	public void setHetFlag(boolean hetFlag) {
		this.hetFlag = hetFlag;
	}

	public List<Integer> getAtomCharges() {
		return atomCharges;
	}
	public void setAtomCharges(List<Integer> atomCharges) {
		this.atomCharges = atomCharges;
	}
	public String getSingleLetterCode() {
		return singleLetterCode;
	}
	public void setSingleLetterCode(String singleLetterCode) {
		this.singleLetterCode = singleLetterCode;
	}
}
