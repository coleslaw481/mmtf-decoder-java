package org.codec.arraydecompressors;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDecompressorTests {

	@Test
	public void runLenghtDecodeIntTest(){

		RunLengthDecode rlds = new RunLengthDecode();
		// Set the size and character
		int numInts = 100;
		int intRep = 1000;
		// Build the test array
		ArrayList<Integer> testArray = new ArrayList<Integer>();
		for(int i=0; i<numInts;i++){
			testArray.add(intRep);
		}
		// Add the lists
		ArrayList<Integer> otherArray = new ArrayList<Integer>();
		otherArray.add(intRep);
		otherArray.add(numInts);
		// Check theyr'e the same
		assertEquals(testArray, rlds.decompressIntArray(otherArray));
	}

	@Test
	public void runLenghtDecodeStringTest(){
		
		RunLengthDecodeString rlds = new RunLengthDecodeString();
		// Set the size and character
		int numChars = 100;
		String charRep = "A";
		// Build the test array
		ArrayList<String> testArray = new ArrayList<String>();
		for(int i=0; i<numChars;i++){
			testArray.add(charRep);
		}
		// Add the lists
		ArrayList<String> otherArray = new ArrayList<String>();
		otherArray.add(charRep);
		otherArray.add(Integer.toString(numChars));
		// Check theyr'e the same
		assertEquals(testArray, rlds.deCompressStringArray(otherArray));
	}

	@Test
	public void deltaDecompressorTest() throws IOException {

		DeltaDeCompress ddc = new DeltaDeCompress();
		// Now let's generate the byte arrays for the test data
		ByteArrayOutputStream bigBos = new ByteArrayOutputStream();
		DataOutputStream bigDos = new DataOutputStream(bigBos);
		ByteArrayOutputStream littleBos = new ByteArrayOutputStream();
		DataOutputStream littleDos = new DataOutputStream(littleBos);

		// Set the size of the start and lenght of the aray
		int initNum = 3000;
		int totalList = 100;
		// Make the big byte array
		bigDos.writeInt(initNum);
		bigDos.writeInt(totalList);
		// Now write the shorts
		for(int i=0; i<totalList; i++){
			littleDos.writeShort(1);
		}

		// Get the test array
		int[] testArray = new int[totalList+1];
		testArray[0] = initNum;
		for(int i=1; i<totalList+1;i++){
			initNum+=1;
			testArray[i] = initNum;
		}
		// Now proccess these
		int[] outArray = ddc.decompressByteArray(bigBos.toByteArray(), littleBos.toByteArray());
		// Check if there the same
		System.out.println(outArray[1]);
		assertArrayEquals(outArray, testArray);
	}

}
