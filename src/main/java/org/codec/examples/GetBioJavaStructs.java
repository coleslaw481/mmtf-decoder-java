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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.biojava.nbio.structure.Structure;
import org.codec.decoder.BioJavaStructureInflator;
import org.codec.decoder.DecodeStructure;

/**
 * Some helper functions and utilit functions to get structures from BioJava - really just for canary release and testing
 * @author anthony
 *
 */
public class GetBioJavaStructs {
	// The base URL used to get data
	private String baseUrl = "http://mmtf.rcsb.org/full/";


	/**
	 * 
	 * @param inputCode
	 * @return A biojava structure object
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public Structure getFromUrl(String inputCode) throws MalformedURLException, IOException{
		// First try to get it from a local file
		String basePath = System.getProperty("PDB_CACHE_DIR");
		if(basePath==null){
			System.out.println("PDB_CACHE_DIR not available");
			basePath = System.getProperty("PDB_DIR");
		}
		boolean isFile = getFile(basePath, inputCode);
		if(isFile==true){
			getFromFileSystem(basePath, inputCode);
		}
		else{
			
		}
		
		DecodeStructure ds = new DecodeStructure();
		BioJavaStructureInflator bjs = new BioJavaStructureInflator();
		// Get these as an inputstream
		byte[] b = IOUtils.toByteArray((new URL(baseUrl+inputCode)).openStream());
		// Cache this
		cacheFile(b, basePath, inputCode);
		// Now get the actual structure
		ds.getStructFromByteArray(deflateGzip(b), bjs);
		return bjs.getStructure();
	}

	private boolean getFile(String basePath, String pdbId) {
		// 
		if(basePath==null){
			return false;
		}
		else{
			String totPath = basePath+"/data/structures/divided/msgpack/"+pdbId.substring(1,3)+"/"+pdbId+".mmtf";
			File thisFile = new File(totPath);
			if(thisFile.exists()){
				return true;
			}
			else{
				return false;
			}
		}
		
		
	}

	private void cacheFile(byte[] b, String basePath, String pdbId) throws IOException {
		// Set the path for the file
		if(basePath==null){
			System.out.println("Not caching - PDB_DIR and PDB_CACHE_DIR not specified");
			return;
		}
		String dirPath = basePath+"/data/structures/divided/msgpack/"+pdbId.substring(1,3)+"/";
		String filePath = dirPath+pdbId+".mmtf";

		File thisFile = new File(dirPath);
		thisFile.mkdirs();
		FileUtils.writeByteArrayToFile(new File(filePath), b);
	}

	/**
	 * Function to get a file from the file system
	 * @param basePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Structure getFromFileSystem(String basePath, String pdbCode) throws FileNotFoundException, IOException{
		// Now return this
		return getFromFileSystem(basePath+"/data/structures/divided/msgpack"+"/"+pdbCode.substring(1, 3)+"/"+pdbCode+".mmtf");
	}


	/**
	 * Function to get a file from the file system - full path supplied
	 * @param basePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Structure getFromFileSystem(String fullPath) throws FileNotFoundException, IOException{
		DecodeStructure ds = new DecodeStructure();
		BioJavaStructureInflator bjs = new BioJavaStructureInflator();
		// Get these as an inputstream
		byte[] b = deflateGzip(IOUtils.toByteArray(new FileInputStream(new File(fullPath))));
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
