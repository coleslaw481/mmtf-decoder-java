package org.codec.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4d;

import org.biojava.nbio.structure.AminoAcid;
import org.biojava.nbio.structure.AminoAcidImpl;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.AtomImpl;
import org.biojava.nbio.structure.BondImpl;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.ChainImpl;
import org.biojava.nbio.structure.Element;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.HetatomImpl;
import org.biojava.nbio.structure.NucleotideImpl;
import org.biojava.nbio.structure.PDBCrystallographicInfo;
import org.biojava.nbio.structure.PDBHeader;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureImpl;
import org.biojava.nbio.structure.StructureTools;
import org.biojava.nbio.structure.io.mmcif.model.ChemComp;
import org.biojava.nbio.structure.quaternary.BioAssemblyInfo;
import org.biojava.nbio.structure.quaternary.BiologicalAssemblyTransformation;
import org.biojava.nbio.structure.xtal.CrystalCell;
import org.biojava.nbio.structure.xtal.SpaceGroup;



public class BioJavaStructureInflator implements StructureInflatorInterface {
	private Structure structure;
	private int modelNumber = 0;
	private int atomCount = 1;
	private Chain chain;
	private Group group;
	private ChemComp cc = new ChemComp();

	
	public BioJavaStructureInflator() {
		structure = new StructureImpl();
	}

	public Structure getStructure() {
		return structure;
	}

	/** 
	 * In biojava this function does nothing
	 */
	public void setModelCount(int inputModelCount) {
		atomCount = 1;
	}

	public void setModelInfo(int inputModelNumber, int chainCount) {
		//		System.out.println("modelNumber: " + modelNumber + " chainCount: " + chainCount);
		modelNumber = inputModelNumber;
		atomCount = 1;
		structure.addModel(new ArrayList<Chain>(chainCount));
	}

	public void setChainInfo(String chainId, int groupCount) {
		//		System.out.println("chainId: " + chainId + " groupCount: " + groupCount);
		chain = new ChainImpl();
		chain.setChainID(chainId.trim());
		structure.addChain(chain, modelNumber);
	}

	public void setGroupInfo(String groupName, int groupNumber,
			char insertionCode, int polymerType, int atomCount) {
		//		System.out.println("groupName: " + groupName + " groupNumber: " + groupNumber + " insertionCode: " + insertionCode + " polymerType: " + polymerType + " atomCount: " + atomCount);
		switch (polymerType) {
		case 1: 
			AminoAcid aa = new AminoAcidImpl();
			// Now set the one letter code
			aa.setAminoType(StructureTools.get1LetterCodeAmino(groupName));
			group = aa;
			break;
		case 2:
			group = new NucleotideImpl();
			break;
		default:
			group = new HetatomImpl();
		}
		// Set the CC -> empty but not null
		group.setChemComp(cc);
		group.setPDBName(groupName);
		group.setResidueNumber(chain.getChainID().trim(), groupNumber, insertionCode);
		group.setAtoms(new ArrayList<Atom>(atomCount));
		//	System.out.println("getting group: " + group);
		if (polymerType != 0) {
			chain.getSeqResGroups().add(group);
		}
		if (atomCount > 0) {

			chain.addGroup(group);	
		}
	}


	public void setAtomInfo(String atomName, int serialNumber, char alternativeLocationId, float x,
			float y, float z, float occupancy, float temperatureFactor,
			String element, int charge) {
		Atom atom = new AtomImpl();	
		atom.setPDBserial(atomCount++);
		//		System.out.println("BioJavaStructureInflator: " + atomCount);
		atom.setName(atomName.trim());
		atom.setElement(Element.valueOfIgnoreCase(element));
		atom.setAltLoc(alternativeLocationId);
		atom.setX(x);
		atom.setY(y);
		atom.setZ(z);
		atom.setOccupancy(occupancy);
		atom.setTempFactor(temperatureFactor);
		atom.setCharge((short) charge);
		//		System.out.println(atom);
		group.addAtom(atom);
	}

	/**
	 * Function to create bonds in the biojava structure objects
	 * @param indOne The first atom's index
	 * @param indTwo The second atom's index
	 * @param bondOrder The bond order
	 */
	public void setBondOrders(int indOne, int indTwo, int bondOrder){
		// Get the atom
		Atom atomOne = group.getAtom(indOne);
		Atom atomTwo = group.getAtom(indTwo);
		// set the new bond
		@SuppressWarnings("unused")
		BondImpl bond = new BondImpl(atomOne, atomTwo, bondOrder);
		
	}
	
	/**
	 * Function to set the bioassmebly info
	 * @param inputAssembly
	 */
	public void setBioAssembly(Map<Integer, Integer> keyList, Map<Integer, Integer> sizeList, Map<Integer, List<String>> inputIds, Map<Integer, List<String>> inputChainIds, Map<Integer, List<double[]>> inputTransformations){
		PDBHeader pdbHeader = structure.getPDBHeader();
		// Get the bioassebly data
		Map<Integer,BioAssemblyInfo> bioAssemblies = new HashMap<Integer,BioAssemblyInfo>();
		for(Integer key: keyList.keySet()){
			// Make a biojava bioassembly object
			BioAssemblyInfo bioAssInfo = new  BioAssemblyInfo();
			bioAssInfo.setId(keyList.get(key));
			// Set size
			bioAssInfo.setMacromolecularSize(sizeList.get(key));
			// Now get the new sizes
			List<BiologicalAssemblyTransformation> newList = new ArrayList<BiologicalAssemblyTransformation>();
			for(int i=0;i<inputIds.get(key).size();i++){
				BiologicalAssemblyTransformation bioAssTrans = new BiologicalAssemblyTransformation();
				bioAssTrans.setId(inputIds.get(key).get(i));
				bioAssTrans.setChainId(inputChainIds.get(key).get(i));
				// Now set matrix
				Matrix4d mat4d = new Matrix4d(inputTransformations.get(key).get(i));
				bioAssTrans.setTransformationMatrix(mat4d);
				// Now add this
				newList.add(bioAssTrans);
			}
			// Now set the transform list
			bioAssInfo.setTransforms(newList);
			// Now set this
			bioAssemblies.put(key, bioAssInfo);
		}
		// Now actually set them
		pdbHeader.setBioAssemblies(bioAssemblies);
		structure.setPDBHeader(pdbHeader);
	}
	
	
	/**
	 * Function to set the crystallographic information
	 */
	public void setXtalInfo(String spaceGroup, List<Float> unitCell){
		// Now set the xtalographic information
		PDBCrystallographicInfo pci = new PDBCrystallographicInfo();
		SpaceGroup G = SpaceGroup.parseSpaceGroup(spaceGroup);
		pci.setSpaceGroup(G);
		if(unitCell.size()==6){
			CrystalCell cell = new CrystalCell(unitCell.get(0), unitCell.get(1), unitCell.get(2), unitCell.get(3), unitCell.get(4), unitCell.get(5));
			pci.setCrystalCell(cell);
			structure.setCrystallographicInfo(pci);
		}

	}
	

}
