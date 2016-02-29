package org.codec.dataholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class to store the data sent in an MMTF data source
 * @author anthony
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MmtfBean {
	
	// The version of the format
	private String mmtfVersion = "0.1";
	// The producer
	private String mmtfProducer;
	// The number of bonds
	private int numBonds;
	// The PDBCode
	private String pdbId;
	// The title of the structure
	private String title;
	// Total data for memory allocation
	private int numAtoms;
	// Add this to store the model information
	private int[] chainsPerModel;
	// Add this to store the model information
	private int[] internalChainsPerModel;
	// List to store the chainids
	private byte[] chainList;
	// List to store the internal chain ids
	private byte[] internalChainList;
	// List to store the number of groups per chain
	private int[] groupsPerChain;
	// List to store the number of groups per internal chain
	private int[] internalGroupsPerChain;
	// String for the space group
	private String spaceGroup;
	// The unit cell information
	private List<Float> unitCell = new ArrayList<Float>(); 
	// A map of Bioassembly -> new class so serializable
	private Map<Integer, BioAssemblyInfoNew> bioAssembly = new HashMap<Integer, BioAssemblyInfoNew>(); 
	// The bond indices and order 
	private byte[] bondAtomList;
	private byte[] bondOrderList;
	// Map of all the data
	private  Map<Integer, PDBGroup> groupMap = new HashMap<Integer, PDBGroup>();
	// For the big arrays split into two -> one of 32 bit ints, one of 16
	private byte[] xCoordBig;
	private byte[] yCoordBig;
	private byte[] zCoordBig;
	private byte[] bFactorBig;
	// Now for the small ints -> 16 bit
	private byte[] xCoordSmall;
	private byte[] yCoordSmall;
	private byte[] zCoordSmall;
	private byte[] bFactorSmall;
	// The secondary structure -> stored as 8 bit ints
	private byte[] secStructList;
	// Run length encoded 
	private byte[] occList;
	// Run length encoded
	private List<String> altLabelList= new ArrayList<String>();
	// Run length encoded
	private List<String> insCodeList = new ArrayList<String>();
	// Delta and run length encoded
	private byte[] groupTypeList;
	// Delta and run length
	private byte[]  groupNumList;
	// Delta and run length encoded
	private byte[] atomIdList; 

	public String getSpaceGroup() {
		return spaceGroup;
	}
	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}
	public List<Float> getUnitCell() {
		return unitCell;
	}
	public void setUnitCell(List<Float> unitCell) {
		this.unitCell = unitCell;
	}

	public byte[] getGroupNumList() {
		return groupNumList;
	}	
	public void setGroupNumList(byte[] _atom_site_auth_seq_id) {
		this.groupNumList = _atom_site_auth_seq_id;
	}
	public byte[] getxCoordBig() {
		return xCoordBig;
	}
	public void setxCoordBig(byte[] cartn_x_big) {
		this.xCoordBig = cartn_x_big;
	}
	public byte[] getyCoordBig() {
		return yCoordBig;
	}
	public void setyCoordBig(byte[] cartn_y_big) {
		this.yCoordBig = cartn_y_big;
	}
	public byte[] getzCoordBig() {
		return zCoordBig;
	}
	public void setzCoordBig(byte[] cartn_z_big) {
		this.zCoordBig = cartn_z_big;
	}
	public byte[] getxCoordSmall() {
		return xCoordSmall;
	}
	public void setxCoordSmall(byte[] cartn_x_small) {
		this.xCoordSmall = cartn_x_small;
	}
	public byte[] getyCoordSmall() {
		return yCoordSmall;
	}
	public void setyCoordSmall(byte[] cartn_y_small) {
		this.yCoordSmall = cartn_y_small;
	}
	public byte[] getzCoordSmall() {
		return zCoordSmall;
	}
	public void setzCoordSmall(byte[] cartn_z_small) {
		this.zCoordSmall = cartn_z_small;
	}
	public byte[] getbFactorBig() {
		return bFactorBig;
	}
	public void setbFactorBig(byte[] b_factor_big) {
		this.bFactorBig = b_factor_big;
	}
	public byte[] getbFactorSmall() {
		return bFactorSmall;
	}
	public void setbFactorSmall(byte[] b_factor_small) {
		this.bFactorSmall = b_factor_small;
	}
	public List<String> getAltLabelList() {
		return altLabelList;
	}
	public void setAltLabelList(List<String> _atom_site_label_alt_id) {
		this.altLabelList = _atom_site_label_alt_id;
	}

	public Map<Integer, BioAssemblyInfoNew> getBioAssembly() {
		return bioAssembly;
	}
	public int[] getChainsPerModel() {
		return chainsPerModel;
	}
	public void setChainsPerModel(int[] chainsPerModel) {
		this.chainsPerModel = chainsPerModel;
	}
	public byte[] getChainList() {
		return chainList;
	}
	public void setChainList(byte[] chainList) {
		this.chainList = chainList;
	}
	public int[] getGroupsPerChain() {
		return groupsPerChain;
	}
	public void setGroupsPerChain(int[] groupsPerChain) {
		this.groupsPerChain = groupsPerChain;
	}
	public void setBioAssembly(Map<Integer, BioAssemblyInfoNew> bioAssembly) {
		this.bioAssembly = bioAssembly;
	}

	public int getNumAtoms() {
		return numAtoms;
	}
	public void setNumAtoms(int numAtoms) {
		this.numAtoms = numAtoms;
	}
	public byte[] getOccList() {
		return occList;
	}
	public void setOccList(byte[] occupancy) {
		this.occList = occupancy;
	}
	public List<String> getInsCodeList() {
		return insCodeList;
	}
	public void setInsCodeList(List<String> _atom_site_pdbx_PDB_ins_code) {
		this.insCodeList = _atom_site_pdbx_PDB_ins_code;
	}
	public Map<Integer, PDBGroup> getGroupMap() {
		return groupMap;
	}
	public void setGroupMap(Map<Integer, PDBGroup> groupMap) {
		this.groupMap = groupMap;
	}
	public byte[] getSecStructList() {
		return secStructList;
	}
	public void setSecStructList(byte[] secStruct) {
		this.secStructList = secStruct;
	}

	public byte[] getGroupTypeList() {
		return groupTypeList;
	}
	public void setGroupTypeList(byte[] resOrder) {
		this.groupTypeList = resOrder;
	}

	public byte[] getAtomIdList() {
		return atomIdList;
	}
	public void setAtomIdList(byte[] _atom_site_id) {
		this.atomIdList = _atom_site_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPdbId() {
		return pdbId;
	}
	public void setPdbId(String pdbCode) {
		this.pdbId = pdbCode;
	}
	public String getMmtfProducer() {
		return mmtfProducer;
	}
	public void setMmtfProducer(String mmtfProducer) {
		this.mmtfProducer = mmtfProducer;
	}
	public String getMmtfVersion() {
		return mmtfVersion;
	}
	public void setMmtfVersion(String mmtfVersion) {
		this.mmtfVersion = mmtfVersion;
	}
	public int getNumBonds() {
		return numBonds;
	}
	public void setNumBonds(int numBonds) {
		this.numBonds = numBonds;
	}
	public byte[] getBondAtomList() {
		return bondAtomList;
	}
	public void setBondAtomList(byte[] bondAtomList) {
		this.bondAtomList = bondAtomList;
	}
	public byte[] getBondOrderList() {
		return bondOrderList;
	}
	public void setBondOrderList(byte[] bondOrderList) {
		this.bondOrderList = bondOrderList;
	}
	public int[] getInternalChainsPerModel() {
		return internalChainsPerModel;
	}
	public void setInternalChainsPerModel(int[] internalChainsPerModel) {
		this.internalChainsPerModel = internalChainsPerModel;
	}
	public int[] getInternalGroupsPerChain() {
		return internalGroupsPerChain;
	}
	public void setInternalGroupsPerChain(int[] internalGroupsPerChain) {
		this.internalGroupsPerChain = internalGroupsPerChain;
	}
	public byte[] getInternalChainList() {
		return internalChainList;
	}
	public void setInternalChainList(byte[] internalChainList) {
		this.internalChainList = internalChainList;
	}

}
