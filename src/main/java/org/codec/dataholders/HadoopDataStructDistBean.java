package org.codec.dataholders;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biojava.nbio.structure.quaternary.BioAssemblyInfo;

public class HadoopDataStructDistBean {
	// The PDBCode
	private String pdbCode;
	// The title of the structure
	private String title;
	// OTHER PDB HEADER INFORMATION
	private String description;
	
	private String classification;	
	private Date depDate;
	private Date modDate;
	private float resolution;
	private float rFree;
	private String doi;
	private String authors;
	// Total data for memory allocation
	private int numAtoms;
	// Add this to store the model information
	private int[] chainsPerModel;
	// List to store the chainids
	private byte[] chainList;
	// List to store the number of groups per chain
	private int[] groupsPerChain;
	// String for the space group
	private String spaceGroup;
	// The unit cell information
	private List<Double> unitCell = new ArrayList<Double>(); 
	// A map of Bioassembly -> new class so serializable
	private Map<Integer, BioAssemblyInfoNew> bioAssembly = new HashMap<Integer, BioAssemblyInfoNew>(); 
//	// The bond indices and order -> NOT FOR NOW
//	private List<Integer> interGroupBondInds = new ArrayList<Integer>();
//	private List<Integer> interGroupBondOrders = new ArrayList<Integer>();
	// Map of all the data
	private  Map<Integer, PDBGroup> groupMap = new HashMap<Integer, PDBGroup>();
	
	// For the big arrays split into two -> one of 32 bit ints, one of 16
	private byte[] cartn_x_big;
	private byte[] cartn_y_big;
	private byte[] cartn_z_big;
	private byte[] b_factor_big;
	// Now for the small ints -> 16 bit
	private byte[] cartn_x_small;
	private byte[] cartn_y_small;
	private byte[] cartn_z_small;
	private byte[] b_factor_small;
	
	
	// The secondary structure -> stored as 8 bit ints
	private byte[] secStruct;
	
	// Run length encoded 
	private byte[] occupancy;
	// Run length encoded
	private List<String> _atom_site_label_alt_id= new ArrayList<String>();
	// Run length encoded
	private List<String> _atom_site_pdbx_PDB_ins_code = new ArrayList<String>();
	// Run length encoded
	private List<Integer> _atom_site_pdbx_PDB_model_num =new ArrayList<Integer>();
	
	
	// Delta and run length encoded
	private byte[] resOrder;
	// Delta and run length
	private byte[]  _atom_site_auth_seq_id;
	// Delta and run length encoded
	private byte[] _atom_site_label_entity_poly_seq_num;
	// Delta and run length encoded
	private byte[] _atom_site_id; 


	public String getSpaceGroup() {
		return spaceGroup;
	}
	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}
	public List<Double> getUnitCell() {
		return unitCell;
	}
	public void setUnitCell(List<Double> unitCell) {
		this.unitCell = unitCell;
	}

	public byte[] get_atom_site_auth_seq_id() {
		return _atom_site_auth_seq_id;
	}	
	public void set_atom_site_auth_seq_id(byte[] _atom_site_auth_seq_id) {
		this._atom_site_auth_seq_id = _atom_site_auth_seq_id;
	}
	public byte[] getCartn_x_big() {
		return cartn_x_big;
	}
	public void setCartn_x_big(byte[] cartn_x_big) {
		this.cartn_x_big = cartn_x_big;
	}
	public byte[] getCartn_y_big() {
		return cartn_y_big;
	}
	public void setCartn_y_big(byte[] cartn_y_big) {
		this.cartn_y_big = cartn_y_big;
	}
	public byte[] getCartn_z_big() {
		return cartn_z_big;
	}
	public void setCartn_z_big(byte[] cartn_z_big) {
		this.cartn_z_big = cartn_z_big;
	}
	public byte[] getCartn_x_small() {
		return cartn_x_small;
	}
	public void setCartn_x_small(byte[] cartn_x_small) {
		this.cartn_x_small = cartn_x_small;
	}
	public byte[] getCartn_y_small() {
		return cartn_y_small;
	}
	public void setCartn_y_small(byte[] cartn_y_small) {
		this.cartn_y_small = cartn_y_small;
	}
	public byte[] getCartn_z_small() {
		return cartn_z_small;
	}
	public void setCartn_z_small(byte[] cartn_z_small) {
		this.cartn_z_small = cartn_z_small;
	}
	public byte[] getB_factor_big() {
		return b_factor_big;
	}
	public void setB_factor_big(byte[] b_factor_big) {
		this.b_factor_big = b_factor_big;
	}
	public byte[] getB_factor_small() {
		return b_factor_small;
	}
	public void setB_factor_small(byte[] b_factor_small) {
		this.b_factor_small = b_factor_small;
	}
	public byte[] get_atom_site_label_entity_poly_seq_num() {
		return _atom_site_label_entity_poly_seq_num;
	}
	public void set_atom_site_label_entity_poly_seq_num(byte[] _atom_site_label_entity_poly_seq_num) {
		this._atom_site_label_entity_poly_seq_num = _atom_site_label_entity_poly_seq_num;
	}
	public List<String> get_atom_site_label_alt_id() {
		return _atom_site_label_alt_id;
	}
	public void set_atom_site_label_alt_id(List<String> _atom_site_label_alt_id) {
		this._atom_site_label_alt_id = _atom_site_label_alt_id;
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
	public byte[] getOccupancy() {
		return occupancy;
	}
	public void setOccupancy(byte[] occupancy) {
		this.occupancy = occupancy;
	}
	public List<String> get_atom_site_pdbx_PDB_ins_code() {
		return _atom_site_pdbx_PDB_ins_code;
	}
	public void set_atom_site_pdbx_PDB_ins_code(List<String> _atom_site_pdbx_PDB_ins_code) {
		this._atom_site_pdbx_PDB_ins_code = _atom_site_pdbx_PDB_ins_code;
	}
	public List<Integer> get_atom_site_pdbx_PDB_model_num() {
		return _atom_site_pdbx_PDB_model_num;
	}
	public void set_atom_site_pdbx_PDB_model_num(List<Integer> _atom_site_pdbx_PDB_model_num) {
		this._atom_site_pdbx_PDB_model_num = _atom_site_pdbx_PDB_model_num;
	}
	public Map<Integer, PDBGroup> getGroupMap() {
		return groupMap;
	}
	public void setGroupMap(Map<Integer, PDBGroup> groupMap) {
		this.groupMap = groupMap;
	}
	public byte[] getSecStruct() {
		return secStruct;
	}
	public void setSecStruct(byte[] secStruct) {
		this.secStruct = secStruct;
	}

	public byte[] getResOrder() {
		return resOrder;
	}
	public void setResOrder(byte[] resOrder) {
		this.resOrder = resOrder;
	}

	public byte[] get_atom_site_id() {
		return _atom_site_id;
	}
	public void set_atom_site_id(byte[] _atom_site_id) {
		this._atom_site_id = _atom_site_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public Date getDepDate() {
		return depDate;
	}
	public void setDepDate(Date depDate) {
		this.depDate = depDate;
	}
	public Date getModDate() {
		return modDate;
	}
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}
	public float getResolution() {
		return resolution;
	}
	public void setResolution(float resolution) {
		this.resolution = resolution;
	}
	public float getrFree() {
		return rFree;
	}
	public void setrFree(float rFree) {
		this.rFree = rFree;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPdbCode() {
		return pdbCode;
	}
	public void setPdbCode(String pdbCode) {
		this.pdbCode = pdbCode;
	}
}
