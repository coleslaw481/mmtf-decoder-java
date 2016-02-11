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
import org.biojava.nbio.structure.Bond;
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

// 
import org.codec.dataholders.BioAssemblyInfoNew;
import org.codec.dataholders.BiologicalAssemblyTransformationNew;
import org.codec.dataholders.HadoopDataStructDistBean;


public class BioJavaStructureInflator implements StructureInflatorInterface {
	private Structure structure;
	private int modelCount = 0;
	private int modelNumber = 0;
	private int atomCount = 1;
	private Chain chain;
	private Group group;
	private ChemComp cc = new ChemComp();

	public BioJavaStructureInflator() {
		this.structure = new StructureImpl();
	}

	public Structure getStructure() {
		return this.structure;
	}

	public void setModelCount(int modelCount) {
		//		System.out.println("model count: " + modelCount);
		this.atomCount = 1;
		this.modelCount = modelCount;
	}

	public void setModelInfo(int modelNumber, int chainCount) {
		//		System.out.println("modelNumber: " + modelNumber + " chainCount: " + chainCount);
		this.modelNumber = modelNumber;
		this.atomCount = 1;
		this.structure.addModel(new ArrayList<Chain>(chainCount));
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

	public void setBondOrders(int indOne, int indTwo, int bondOrder){
		// Get the atom
		Atom atomOne = group.getAtom(indOne);
		Atom atomTwo = group.getAtom(indTwo);
		// set the new bond
		Bond newBond = new BondImpl(atomOne, atomTwo, bondOrder);
		
	}
	
	
	
	public void setOtherInfo(HadoopDataStructDistBean xs) {

		// Get the BioAssemly data
		Map<Integer, BioAssemblyInfoNew> bioData = xs.getBioAssembly();
		// Get the Xtalographic info
		String spaceGroup = xs.getSpaceGroup();
		List<Double> unitCell = xs.getUnitCell();
		// Get the header
		PDBHeader pdbHeader = this.structure.getPDBHeader();
		// 
		Map<Integer, BioAssemblyInfoNew> oldAss = xs.getBioAssembly();
		Map<Integer,BioAssemblyInfo> bioAssemblies = new HashMap<Integer,BioAssemblyInfo>();
		for(Integer key: oldAss.keySet()){
			// Get the old info
			BioAssemblyInfoNew bioAssOld = oldAss.get(key);
			// Make a new one
			BioAssemblyInfo bioAssInfo = new  BioAssemblyInfo();
			bioAssInfo.setId(bioAssOld.getId());
			// Set this size
			bioAssInfo.setMacromolecularSize(bioAssOld.getMacromolecularSize());
			// Now get the new sizes
			List<BiologicalAssemblyTransformation> newList = new ArrayList<BiologicalAssemblyTransformation>();
			for(BiologicalAssemblyTransformationNew bioAssTransOld: bioAssOld.getTransforms()){
	
				BiologicalAssemblyTransformation bioAssTrans = new BiologicalAssemblyTransformation();
				bioAssTrans.setId(bioAssTransOld.getId());
				bioAssTrans.setChainId(bioAssTransOld.getChainId());
				// Now set this matrix
				Matrix4d mat4d = new Matrix4d(bioAssTransOld.getTransformation());
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
		this.structure.setPDBHeader(pdbHeader);

		// Now set the xtalographic information
		PDBCrystallographicInfo pci = new PDBCrystallographicInfo();
		SpaceGroup thisSG = SpaceGroup.parseSpaceGroup(spaceGroup);
		pci.setSpaceGroup(thisSG);
		if(unitCell.size()==6){
			CrystalCell cell = new CrystalCell(unitCell.get(0), unitCell.get(1), unitCell.get(2), unitCell.get(3), unitCell.get(4), unitCell.get(5));
			pci.setCrystalCell(cell);
			this.structure.setCrystallographicInfo(pci);
		}

	}
}
