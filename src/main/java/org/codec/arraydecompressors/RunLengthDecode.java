package org.codec.arraydecompressors;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RunLengthDecode implements IntArrayDeCompressor{
	public ArrayList<Integer> decompressIntArray(ArrayList<Integer> inArray) {
		// TODO Auto-generated method stub
		ArrayList<Integer> outArray =  new ArrayList<Integer>();
		// Loop through the vals
		for (int i = 0; i < inArray.size(); i+=2) {
			// Get the value out here
			int intIn = inArray.get(i);
			int numOfInt = inArray.get(i+1);
			// Now add these to the array
			for (int j=0; j<numOfInt; j++){
				outArray.add(intIn);
			}
		}
		return outArray;
	}


	public int[] decompressByteArray(byte[] inArray) throws IOException {

		// Array to store all the different numbers
		int[] numArr = new int[inArray.length/8];
		int[] countArr = new int[inArray.length/8];
		// Get the size
		int totCount = 0;
		DataInputStream bis = new DataInputStream(new ByteArrayInputStream(inArray));
		for(int i=0; i< inArray.length/8; i++){
			// Get the number
			int getNum = bis.readInt();
			int getCount = bis.readInt();
			totCount += getCount;
			numArr[i] = getNum;
			countArr[i] = getCount;
		}
		// Now set this output array
		int[] outArr = new int[totCount];
		int totCounter =0;
		for(int i=0;i<numArr.length;i++){
			// 
			int thisAns = numArr[i];
			for(int j=0; j<countArr[i]; j++){
				// Annd then add t is to the array
				outArr[totCounter] = thisAns;
				// Now add to the counter
				totCounter++;

			}

		}
		return outArr;
	}

}
