package org.codec.examples;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.biojava.nbio.structure.Structure;
import org.codec.decoder.BioJavaStructureInflator;
import org.codec.decoder.DecodeStructure;

public class GetBioJavaStructs {
	// The base URL used to get data
	private String baseUrl = "http://132.249.213.68:8080/servemessagepack/";

	
	/**
	 * 
	 * @param inputCode
	 * @return A biojava structure object
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public Structure getFromUrl(String inputCode) throws MalformedURLException, IOException{
		DecodeStructure ds = new DecodeStructure();
		BioJavaStructureInflator bjs = new BioJavaStructureInflator();

		// Get these as an inputstream
		byte[] b = IOUtils.toByteArray((new URL(baseUrl+inputCode)).openStream()); 
		// Now get the actual structure
		ds.getStructFromByteArray(b, bjs);
		return bjs.getStructure();
	}
	
	/**
	 * Function to get a file from the file system
	 * @param basePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Structure getFromFileSystem(String basePath, String pdbCode) throws FileNotFoundException, IOException{
		DecodeStructure ds = new DecodeStructure();
		BioJavaStructureInflator bjs = new BioJavaStructureInflator();
		// Get these as an inputstream
		byte[] b = deflateGzip(IOUtils.toByteArray(new FileInputStream(new File(basePath+"/"+pdbCode.substring(1, 3)+"/"+pdbCode))));
		// Now get the actual structure
		ds.getStructFromByteArray(b, bjs);
		return bjs.getStructure();
	}
	


	/**
	 * 
	 * @param inputBytes -> gzip compressed byte
	 * array
	 * @return A deflated byte array
	 * @throws IOException
	 */
	private byte[] deflateGzip(byte[] inputBytes) throws IOException{

		ByteArrayInputStream bis = new ByteArrayInputStream(inputBytes);
		GZIPInputStream gis = new GZIPInputStream(bis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// Make a buffer
		byte[] tmp = new byte[4*1024];
		try{
			while(gis.available()==1){
				int size = gis.read(tmp);
				baos.write(tmp, 0, size);
			}
		} catch (Exception ex){

		} finally {
			try{
				if(baos != null) baos.close();
			} catch(Exception ex){}
		}
		// Get the bytes
		return baos.toByteArray();
	}

}
