package org.codec.decoder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.biojava.nbio.structure.Structure;
import org.codec.dataholders.HadoopDataStructDistBean;
import org.codec.dataholders.PDBGroup;


import org.codec.arraydecompressors.DeltaDeCompress;
import org.codec.arraydecompressors.RunLengthDecode;
import org.codec.arraydecompressors.RunLengthDelta;
import org.codec.arraydecompressors.RunLengthDecodeString;


import org.msgpack.jackson.dataformat.MessagePackFactory;

public class DecodeStructure {
	
	// 
	public Structure getStructFromByteArray(byte[] myInBytes) throws IOException {
		List<String> nucAcidList = new ArrayList<String>();
//		nucAcidList.add();
		float coordDiv = (float) 1000.0;
		float occDiv = (float) 100.0;
		BioJavaStructureInflator bioJavaStructInflator = new BioJavaStructureInflator();
		// Get the decompression
		DeltaDeCompress delta = new DeltaDeCompress();
		RunLengthDelta intrunlength_delta = new RunLengthDelta();
		RunLengthDecode intrunlength = new RunLengthDecode();
		RunLengthDecodeString stringRunlength = new RunLengthDecodeString();		
		com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new MessagePackFactory());
		HadoopDataStructDistBean xs = objectMapper.readValue(myInBytes, HadoopDataStructDistBean.class);
		// GET THE MODEL LIST AND THE CHAIN MAP
		byte[] chainList = xs.getChainList();
		int[] chainsPerModel = xs.getChainsPerModel();
		int[] groupsPerChain = xs.getGroupsPerChain();
		// Now get the group map
		// NEED TO ADD THIS INFORMATION AS A HEADER
		int[] groupList = bytesToInts(xs.getResOrder());
		int lastCount = 0;
		// Read the byte arrays as int arrays
		// Get the length of the 
		int[] cartnX = delta.decompressByteArray(xs.getCartn_x_big(),xs.getCartn_x_small());
		int[] cartnY = delta.decompressByteArray(xs.getCartn_y_big(),xs.getCartn_y_small());
		int[] cartnZ = delta.decompressByteArray(xs.getCartn_z_big(),xs.getCartn_z_small());
		int[] bFactor =  delta.decompressByteArray(xs.getB_factor_big(),xs.getB_factor_small());
		int[] occupancyArr = intrunlength.decompressByteArray(xs.getOccupancy());
		int[] atomId = intrunlength_delta.decompressByteArray(xs.get_atom_site_id());
		char[] altId = stringRunlength.deCompressStringArrayToChar((ArrayList<String>) xs.get_atom_site_label_alt_id());
		// Get the insertion code
		char[] insCode = stringRunlength.deCompressStringArrayToChar((ArrayList<String>) xs.get_atom_site_pdbx_PDB_ins_code());
		// Get the groupNumber
		int[] groupNum = intrunlength_delta.decompressByteArray(xs.get_atom_site_label_entity_poly_seq_num());
		Map<Integer, PDBGroup> groupMap = xs.getGroupMap();
		int modelCounter = -1;
		int groupCounter = 0;
		int chainCounter = 0;
		for (int modelChains: chainsPerModel){
			modelCounter++;
			bioJavaStructInflator.setModelInfo(modelCounter, modelChains);
			int totChainsThisMod = chainCounter+modelChains;
			for (int thisChain=chainCounter; thisChain<totChainsThisMod; thisChain++){
				// Get the bytes for the  chain 
				// Get a stringbuilder
				StringBuilder sb = new StringBuilder();
				byte chainIdOne = chainList[thisChain*4+0];
				sb.append(chainIdOne);
				byte chainIdTwo = chainList[thisChain*4+1];
				if(chainIdTwo!=(byte) 0){
					sb.append(chainIdTwo);
				}
				byte chainIdThree = chainList[thisChain*4+2];
				if(chainIdThree!=(byte) 0){
					sb.append(chainIdThree);
				}
				byte chainIdFour = chainList[thisChain*4+3];
				if(chainIdFour!=(byte) 0){
					sb.append(chainIdFour);
				}				
				int groupsThisChain = groupsPerChain[thisChain];
				bioJavaStructInflator.setChainInfo(sb.toString(), groupsThisChain);
				int nextInd = groupCounter+groupsThisChain;
				for(int thisGroupNum=groupCounter; thisGroupNum<nextInd;thisGroupNum++){
					groupCounter++;
					// Now get the group
					int g = groupList[thisGroupNum];
					// Get this info
					PDBGroup thisGroup = groupMap.get(g);
					List<String> atomInfo = thisGroup.getAtomInfo();
					int atomCount = atomInfo.size()/2;
					///// NEEDS FIXING FOR NUCLEIC ACIDS... CURRENTLY JUST CONSIDERS AMINO ACIDS AND HET ATOMS
					int thsG = groupNum[lastCount];
					char thsIns = insCode[lastCount];
					/////// OK SO NOW WE NEED A FLAG TO FIND NUCLEIC ACIDS
					if(nucAcidList.contains(thisGroup.getResName())){
						// Now set this as 
						bioJavaStructInflator.setGroupInfo(thisGroup.getResName(), thsG, thsIns, 2, atomCount);
					}
					else{
						bioJavaStructInflator.setGroupInfo(thisGroup.getResName(), thsG, thsIns, (thisGroup.isHetFlag()) ? 0 : 1, atomCount);
					}
					// A counter for the atom information
					int atomCounter = 0;
					System.out.println(thisGroup.getResName());
					System.out.println(g);

					// Now read the next atoms
					for(int i=lastCount; i<lastCount+atomCount;i++){
						// Now get all the relevant information here
						String atomName = atomInfo.get(atomCounter*2+1);
						String element = atomInfo.get(atomCounter*2);
						int charge = thisGroup.getAtomCharges().get(atomCounter);
						int serialNumber = atomId[i];
						char alternativeLocationId = altId[i];
						float x = cartnX[i]/coordDiv;
						float z = cartnZ[i]/coordDiv;
						float y = cartnY[i]/coordDiv;
						float occupancy = occupancyArr[i]/occDiv;
						float temperatureFactor = bFactor[i]/occDiv;
						bioJavaStructInflator.setAtomInfo(atomName, serialNumber, alternativeLocationId, x, y, z, occupancy, temperatureFactor, element, charge);
						atomCounter++;
					}
					lastCount+=atomCount;
					// Now add the bond information for this group
					List<Integer> bondInds = thisGroup.getBondIndices();
					List<Integer> bondOrders = thisGroup.getBondOrders();
					for(int thisBond=0; thisBond<bondOrders.size();thisBond++){
						int thisBondOrder = bondOrders.get(thisBond);
						int thisBondIndOne = bondInds.get(thisBond*2);
						int thisBondIndTwo = bondInds.get(thisBond*2+1);
						bioJavaStructInflator.setBondOrders(thisBondIndOne, thisBondIndTwo, thisBondOrder);
					}
				}
				chainCounter++;
			}
		}

		// Now set this other info
		bioJavaStructInflator.setOtherInfo(xs);
		Structure outStruct = bioJavaStructInflator.getStructure();
		return outStruct;
	}

	private int[] bytesToInts(byte[] inArray) throws IOException {
		// TODO Auto-generated method stub
		DataInputStream bis = new DataInputStream(new ByteArrayInputStream(inArray));

		// Define an array to return
		int[] outArr = new int[inArray.length/4];
		for(int i=0; i<inArray.length/4;i++){
			outArr[i] = bis.readInt();
		}

		return outArr;
	}


}
