package org.codec.dataholders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PDBGroup implements Serializable {
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

	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
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
	// Define these two strings
	private String resName = new String();
	private boolean hetFlag = false;
	private List<String> atomInfo = new ArrayList<String>();
	private List<Integer> bondOrders = new ArrayList<Integer>();
	private List<Integer> bondIndices = new ArrayList<Integer>();
	private List<Integer> atomCharges = new ArrayList<Integer>();
}
