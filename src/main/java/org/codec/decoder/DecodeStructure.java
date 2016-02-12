package org.codec.decoder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codec.dataholders.BioAssemblyInfoNew;
import org.codec.dataholders.HadoopDataStructDistBean;
import org.codec.dataholders.PDBGroup;


import org.codec.arraydecompressors.DeltaDeCompress;
import org.codec.arraydecompressors.RunLengthDecode;
import org.codec.arraydecompressors.RunLengthDelta;
import org.codec.arraydecompressors.RunLengthDecodeString;

import org.msgpack.jackson.dataformat.MessagePackFactory;

public class DecodeStructure {

	/**
	 * Function to generate a structure from bytes using a structure inflator
	 * @param myInBytes
	 * @return
	 * @throws IOException
	 */
	public void getStructFromByteArray(byte[] myInBytes, StructureInflatorInterface structInflator) throws IOException {
		// Create a list of all the nucleic acid ids
		List<String> nucAcidList = new ArrayList<String>();		
		// Set the variables to multiply by
		float coordDiv = (float) 1000.0;
		float occDiv = (float) 100.0;
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
		int[] groupList = bytesToInts(xs.getGroupTypeList());
		int lastCount = 0;
		// Read the byte arrays as int arrays
		// Get the length of the 
		int[] cartnX = delta.decompressByteArray(xs.getxCoordBig(),xs.getxCoordSmall());
		int[] cartnY = delta.decompressByteArray(xs.getyCoordBig(),xs.getyCoordSmall());
		int[] cartnZ = delta.decompressByteArray(xs.getzCoordBig(),xs.getzCoordSmall());
		int[] bFactor =  delta.decompressByteArray(xs.getbFactorBig(),xs.getbFactorSmall());
		int[] occupancyArr = intrunlength.decompressByteArray(xs.getOccList());
		int[] atomId = intrunlength_delta.decompressByteArray(xs.getAtomIdList());
		char[] altId = stringRunlength.deCompressStringArrayToChar((ArrayList<String>) xs.getAltLabelList());
		// Get the insertion code
		char[] insCode = stringRunlength.deCompressStringArrayToChar((ArrayList<String>) xs.getInsCodeList());
		// Get the groupNumber
		int[] groupNum = intrunlength_delta.decompressByteArray(xs.getResNumList());
		Map<Integer, PDBGroup> groupMap = xs.getGroupMap();
		int modelCounter = -1;
		int groupCounter = 0;
		int chainCounter = 0;
		for (int modelChains: chainsPerModel){
			modelCounter++;
			structInflator.setModelInfo(modelCounter, modelChains);
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
				structInflator.setChainInfo(sb.toString(), groupsThisChain);
				int nextInd = groupCounter+groupsThisChain;
				for(int thisGroupNum=groupCounter; thisGroupNum<nextInd;thisGroupNum++){
					groupCounter++;
					// Now get the group
					int g = groupList[thisGroupNum];
					// Get this info
					PDBGroup thisGroup = groupMap.get(g);
					List<String> atomInfo = thisGroup.getAtomInfo();
					int atomCount = atomInfo.size()/2;
					int thsG = groupNum[lastCount];
					char thsIns = insCode[lastCount];
					///// NEEDS FIXING FOR NUCLEIC ACIDS... CURRENTLY JUST CONSIDERS AMINO ACIDS AND HET ATOMS
					/////// OK SO NOW WE NEED A FLAG TO FIND NUCLEIC ACIDS
					if(nucAcidList.contains(thisGroup.getResName())){
						// Now set this as 
						structInflator.setGroupInfo(thisGroup.getResName(), thsG, thsIns, 2, atomCount);
					}
					else{
						structInflator.setGroupInfo(thisGroup.getResName(), thsG, thsIns, (thisGroup.isHetFlag()) ? 0 : 1, atomCount);
					}
					// A counter for the atom information
					int atomCounter = 0;
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
						structInflator.setAtomInfo(atomName, serialNumber, alternativeLocationId, x, y, z, occupancy, temperatureFactor, element, charge);
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
						structInflator.setBondOrders(thisBondIndOne, thisBondIndTwo, thisBondOrder);
					}
				}
				chainCounter++;
			}
		}
		// Now set the crystallographic and the bioassembly information
		structInflator.setXtalInfo(xs.getSpaceGroup(), xs.getUnitCell());
		/// NOW SET ALL THESE INPUT VARS  
		Map<Integer, BioAssemblyInfoNew> bioAss = xs.getBioAssembly();
		// The maps for storing this information
		Map<Integer, Integer> keyList = new HashMap<Integer, Integer>();
		Map<Integer, Integer> sizeList = new HashMap<Integer, Integer>();
		Map<Integer, List<String>> inputIds = new HashMap<Integer, List<String>>();
		Map<Integer, List<String>> inputChainIds = new HashMap<Integer, List<String>>();
		Map<Integer, List<double[]>> inputTransformations = new HashMap<Integer, List<double[]>>();
		for(Integer key: bioAss.keySet()){
			// Get the bioassembly info
			BioAssemblyInfoNew bioAssOld = bioAss.get(key);
		}
		// Set the bioassembly information
		structInflator.setBioAssembly(keyList, sizeList,  inputIds, inputChainIds, inputTransformations);
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
