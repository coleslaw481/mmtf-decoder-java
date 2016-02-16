package org.codec.arraydecompressors;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class DeltaDeCompress {

	public int[] decompressByteArray(byte[] bigNums, byte[] smallNums) throws IOException
	{
		// Get these data streams
		DataInputStream bigStream = new DataInputStream(new ByteArrayInputStream(bigNums));
		int[] bigInts = new int[bigNums.length/8];
		int[] counterInts = new int[bigNums.length/8];
		DataInputStream smallStream = new DataInputStream(new ByteArrayInputStream(smallNums));
		int totNum = 0;
		// Loop through these and take every other int
		for(int i=0; i<bigNums.length/8;i++){
			int bigNum =  bigStream.readInt();
			int counterNum =  bigStream.readInt();
			// Now writ thei sout
			totNum++;
			// Now add to the counter
			totNum += counterNum;
			bigInts[i] = bigNum;
			counterInts[i] = counterNum;
		}
		// Now loop over the total number of ints
		int[] outArr = new int[totNum];
		int totCounter = 0;
		for(int i=0; i<bigInts.length; i++)
		{	
			// Now add this to the out array
			outArr[totCounter] = bigInts[i];
			totCounter++;
			// Now loop through this
			for(int j=0; j<counterInts[i]; j++){
				// Now add this as a short
				outArr[totCounter] = outArr[totCounter-1] + (int) smallStream.readShort();
				totCounter++;

			}
		}
		return outArr;
	}
}
