package org.rcsb.mmtf.dataholders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to store the encoding of secondary structure types
 * @author anthony
 *
 */
public class CodeHolders {

	// A map to indicate secondary structure information
	public Map<String, Integer> dsspMap;
	
	public CodeHolders(){
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		//		aMap.put(key, value);
		aMap.put("pi Helix", 0);
		aMap.put("Bend", 1);
		aMap.put("alpha Helix",2);
		aMap.put("Extended", 3);
		aMap.put("3-10 Helix", 4);
		aMap.put("Bridge", 5);
		aMap.put("Turn", 6);
		aMap.put("Coil",7);
		aMap.put("NA", -1);
		dsspMap = Collections.unmodifiableMap(aMap);
	}
}
