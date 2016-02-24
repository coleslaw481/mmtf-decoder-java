package org.codec.arraydecompressors;

import java.util.List;

public interface IntArrayDeCompressor {
	
	/**
	 * Function to decompress an integer array and return an other integer array
	 * @param inArray
	 * @return
	 */
	public List<Integer> decompressIntArray(List<Integer> inArray);
}
