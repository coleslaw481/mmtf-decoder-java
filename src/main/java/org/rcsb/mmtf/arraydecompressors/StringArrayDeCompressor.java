package org.rcsb.mmtf.arraydecompressors;

import java.util.List;

public interface StringArrayDeCompressor {
	
	/**
	 * Generic function to decompress a list of Strings to another list of Strings
	 * @param inArray
	 * @return
	 */
	public List<String> deCompressStringArray(List<String> inArray);

}
