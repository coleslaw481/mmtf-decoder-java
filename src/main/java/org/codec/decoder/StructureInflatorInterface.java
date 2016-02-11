package org.codec.decoder;


public interface StructureInflatorInterface {

	/**
	 * 
	 * @param modelCount
	 */
	void setModelCount(int modelCount);
	
	/**
	 * Function to set the information for a given model
	 * @param modelNumber
	 * @param chainCount
	 */
	void setModelInfo(int modelNumber, int chainCount);
	
	/**
	 * Function to set the information for a given chain
	 * @param chainId
	 * @param groupCount
	 */
	void setChainInfo(String chainId, int groupCount);
	
	/**
	 * Function to set the information for a given group
	 * 
	 * @param groupName
	 * @param groupNumber
	 * @param insertionCode
	 * @param polymerType
	 * @param atomCount
	 */
	void setGroupInfo(String groupName, int groupNumber, char insertionCode, int polymerType, int atomCount);
	
	/**
	 * 
	 * Function to set the information for a given atom
	 * 
	 * @param atomName
	 * @param serialNumber
	 * @param alternativeLocationId
	 * @param x
	 * @param y
	 * @param z
	 * @param occupancy
	 * @param temperatureFactor
	 * @param element
	 * @param charge
	 */
	void setAtomInfo(String atomName, int serialNumber, char alternativeLocationId, 
			float x, float y, float z, float occupancy, float temperatureFactor, String element, int charge);
	
}
