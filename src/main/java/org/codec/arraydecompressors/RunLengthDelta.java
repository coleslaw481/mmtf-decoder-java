package org.codec.arraydecompressors;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class RunLengthDelta {

	/**
	 * Function to decompress a byte array to an int array
	 * @param inArray
	 * @return
	 * @throws IOException
	 */
	public int[] decompressByteArray(byte[] inArray) throws IOException {
		// Array to store all the different numbers
		int[] numArr = new int[inArray.length/8];
		int[] countArr = new int[inArray.length/8];
		// Get the size
		int totCount = 0;
		DataInputStream bis = new DataInputStream(new ByteArrayInputStream(inArray));
		for(int i=0; i< inArray.length/8; i++){
			// Get the numbers
			int getNum = bis.readInt();
			// Get the number of repeats
			int getCount = bis.readInt();
			if(getCount<0){
				System.out.println(getCount);
			}
			// Get the total count
			totCount += getCount;
			// Fill the number array
			numArr[i] = getNum;
			// Fill the count array
			countArr[i] = getCount;
		}
		// Now set this output array
		int[] outArr = new int[totCount];
		int totCounter =0;
		int totAns = 0;
		for(int i=0;i<numArr.length;i++){
			// Get the number that is to be repeared
			int thisAns = numArr[i];
			// Get the number of repeats
			for(int j=0; j<countArr[i]; j++){
				// Add the delta to get this answer
				totAns+=thisAns;
				// And then add t is to the array
				outArr[totCounter] = totAns;
				// Now add to the counter
				totCounter++;
			}
			
		}
		return outArr;
	}

}
