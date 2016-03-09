package org.rcsb.mmtf.examples;

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

/**
 * Some helper functions and utilit functions to get structures from BioJava.
 * Really just for canary release and testing.
 * @author Anthony Bradley
 *
 */
public class HandleIO {

  /** The base url. */
  private static final String BASE_URL = "http://mmtf.rcsb.org/full/";
  /** The index to get the middle two characters of a PDB id. */
  private static final int END_ID_FOR_MID_PDB = 3;
  /** The size of a chunk for a byte buffer. */
  private static final int BYTE_BUFFER_CHUNK_SIZE = 4096;

  /**
   * Gets the from url.
   *
   * @param inputCode the input code
   * @return A biojava structure object
   * @throws MalformedURLException the malformed url exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public final byte[] getFromUrlOrFile(final String inputCode) {
    String basePath = getBasePath();
    boolean isFile = getFile(basePath, inputCode);
    // If it's a file on the file system - get it
    if (isFile) {
      return getFromFileSystem(basePath, inputCode);
    }
    return getFromUrl(inputCode);
  }
  
  /**
   * Get from a cached file on the file system.
   * @param inputCode The four letter pdb code to fine
   * @return The message pack byte array
   */
  public final byte[] getFromFile(final String inputCode) {
    String basePath = getBasePath();
    String fullPath = constructPath(basePath, inputCode);
    return getFromFileSystem(fullPath);
    

  }

  /**
   * Find the message pack byte array from the web using the input code.
   * Caches the file if possible.
   * @param inputCode
   * @return
   */
  public final byte[] getFromUrl(final String inputCode) {
    // Get the base path
    String basePath = getBasePath();
    // Get these as an inputstream
    byte[] b;
    try {
      b = IOUtils.toByteArray((new URL(BASE_URL + inputCode)).openStream());
    } catch (MalformedURLException e) {
      // 
      System.err.println("Error in forming URL. Returning empty byte array");
      return null;
    } catch (IOException e) {
      System.err.println("Error in getting data from URL. Returning empty byte array");
      return null;
    }
    // Cache the data on the file system
    cacheFile(b, basePath, inputCode);
    // Now return the gzip deflated byte array
    return deflateGzip(b);


  }

  /**
   * Gets the file from the file system. Specify the pdb id and the base path
   *
   * @param basePath the base path
   * @param pdbCode the pdb code
   * @return the from file system
   * @throws FileNotFoundException the file not found exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private final byte[] getFromFileSystem(final String basePath, final String pdbCode) {

    String fullFilePath = constructPath(basePath, pdbCode);
    // Now return the message pack byte[]
    return getFromFileSystem(fullFilePath);
  }

  /**
   * Construct the full path for the file to be stored on the file system.
   * @param basePath The base directory (PDB_DIR in Biojava)
   * @param pdbCode The four letter pdb code
   * @return The full path, including suffic to be written out.
   */
  private String constructPath(String basePath, String pdbCode) {
    return basePath + "/data/structures/divided/msgpack" + "/" + pdbCode.substring(1, END_ID_FOR_MID_PDB) + "/" + pdbCode + ".mmtf";
  }

  /**
   * Function to get a file from the file system - full path supplied.
   *
   * @param fullPath the full path
   * @return the from file system
   * @throws FileNotFoundException the file not found exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private final byte[] getFromFileSystem(final String fullPath) {
    // Get these as an inputstream
    byte[] inputByteArr;
    try {
      inputByteArr = deflateGzip(IOUtils.toByteArray(new FileInputStream(new File(fullPath))));
    } catch (IOException e) {
      System.err.println("Could not find file: "+fullPath);
      return null;
    }
    // Now return it
    return inputByteArr;
  }

  /**
   * Gets the file form the file system
   *
   * @param basePath the base path
   * @param pdbId the pdb id
   * @return the file
   */
  private boolean getFile(final String basePath, final String pdbId) {
    // Set the path for the file
    if (basePath == null) {
      System.out.println("Can't get - PDB_DIR and PDB_CACHE_DIR not specified");
      return false;
    }
    String dirPath = basePath
        + "/data/structures/divided/msgpack/"
        + pdbId.substring(1, END_ID_FOR_MID_PDB) + "/";
    String filePath = dirPath + pdbId + ".mmtf";
    File thisFile = new File(filePath);
    return thisFile.exists();
  }

  /**
   * Cache file.
   *
   * @param b the b
   * @param basePath the base path
   * @param pdbId the pdb id
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void cacheFile(final byte[] b, final String basePath, final String pdbId) {
    // Set the path for the file
    if (basePath == null) {
      System.out.println("Not caching - PDB_DIR and PDB_CACHE_DIR not specified");
      return;
    }
    String dirPath = basePath
        + "/data/structures/divided/msgpack/"
        + pdbId.substring(1, END_ID_FOR_MID_PDB) + "/";
    String filePath = dirPath + pdbId + ".mmtf";

    File thisFile = new File(dirPath);
    boolean success  = thisFile.mkdirs();
    if(success){
      System.out.println("Made base files");
    }
    try {
      FileUtils.writeByteArrayToFile(new File(filePath), b);
    } catch (IOException e) {
      // Error in caching the file
      System.err.println("Error in caching file on file system: " + filePath);
      return;
    }

  }

  /**
   * Deflate a gzip byte array.
   *
   * @param inputBytes -> gzip compressed byte
   * array
   * @return A deflated byte array
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private byte[] deflateGzip(final byte[] inputBytes){
    // Start the byte input stream
    ByteArrayInputStream bis = new ByteArrayInputStream(inputBytes);
    GZIPInputStream gis;
    try {
      gis = new GZIPInputStream(bis);
    } catch (IOException e) {
      System.err.println("Error in opening byte array.");
      e.printStackTrace();
      return null;
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // Make a buffer
    byte[] tmp = new byte[BYTE_BUFFER_CHUNK_SIZE];
    try {
      while (gis.available() == 1) {
        int size = gis.read(tmp);
        if(size==-1){
          break;
        }
        baos.write(tmp, 0, size);
      }
    } 
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    } 
    finally {
      try {
        if (baos != null) {
          baos.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        return null;
      }
    }
    // Get the bytes
    byte[] outArr = baos.toByteArray();
    return outArr;
  }

  /**
   * Get the base path to cache the data in
   * @return The base path to store the data in. Defined by environment variables.
   */
  private String getBasePath() {
    // First try to get it from a local file
    String basePath = System.getProperty("PDB_CACHE_DIR");
    if (basePath == null) {
      System.out.println("PDB_CACHE_DIR not available");
      basePath = System.getProperty("PDB_DIR");
    }
    return basePath;
  }
}
