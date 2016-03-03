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
import org.biojava.nbio.structure.Structure;
import org.rcsb.mmtf.decoder.BioJavaStructureDecoder;
import org.rcsb.mmtf.decoder.DecodeStructure;
import org.rcsb.mmtf.decoder.ParsingParams;

/**
 * Some helper functions and utilit functions to get structures from BioJava.
 * Really just for canary release and testing.
 * @author Anthony Bradley
 *
 */
public class GetBioJavaStructs {

  /** The base url. */
  private static final String BASE_URL = "http://mmtf.rcsb.org/full/";
  /** The index to get the middle two characters of a PDB id. */
  private static final int END_ID_FOR_MID_PDB = 3;
  /** The size of a chunk for a byte buffer. */
  private static final int BYTE_BUFFER_CHUNK_SIZE = 4096;
  /** The parsing parameter settings for the decoder. */
  private ParsingParams pp = new ParsingParams();

  /**
   * Gets the from url.
   *
   * @param inputCode the input code
   * @return A biojava structure object
   * @throws MalformedURLException the malformed url exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public final Structure getFromUrl(final String inputCode)
      throws MalformedURLException, IOException {
    // First try to get it from a local file
    String basePath = System.getProperty("PDB_CACHE_DIR");
    if (basePath == null) {
      System.out.println("PDB_CACHE_DIR not available");
      basePath = System.getProperty("PDB_DIR");
    }
    boolean isFile = getFile(basePath, inputCode);
    // If it's a file on the file system - get it
    if (isFile) {
      return getFromFileSystem(basePath, inputCode);
    }
    DecodeStructure ds = new DecodeStructure();
    BioJavaStructureDecoder bjs = new BioJavaStructureDecoder();
    // Get these as an inputstream
    byte[] b = IOUtils.toByteArray((new URL(BASE_URL
        + inputCode)).openStream());
    // Cache this
    cacheFile(b, basePath, inputCode);
    // Now get the actual structure
    ds.getStructFromByteArray(deflateGzip(b), bjs, pp);
    return bjs.getStructure();
  }

  /**
   * Gets the file.
   *
   * @param basePath the base path
   * @param pdbId the pdb id
   * @return the file
   */
  private boolean getFile(final String basePath, final String pdbId) {
    return !(basePath == null);


  }

  /**
   * Cache file.
   *
   * @param b the b
   * @param basePath the base path
   * @param pdbId the pdb id
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void cacheFile(final byte[] b, final String basePath,
      final String pdbId) throws IOException {
    // Set the path for the file
    if (basePath == null) {
      System.out.println("Not caching - "
          + "PDB_DIR and PDB_CACHE_DIR not specified");
      return;
    }
    String dirPath = basePath
        + "/data/structures/divided/msgpack/"
        + pdbId.substring(1, END_ID_FOR_MID_PDB) + "/";
    String filePath = dirPath + pdbId + ".mmtf";

    File thisFile = new File(dirPath);
    thisFile.mkdirs();
    FileUtils.writeByteArrayToFile(new File(filePath), b);
  }

  /**
   * Function to get a file from the file system.
   *
   * @param basePath the base path
   * @param pdbCode the pdb code
   * @return the from file system
   * @throws FileNotFoundException the file not found exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public final Structure getFromFileSystem(final String basePath,
      final String pdbCode) throws FileNotFoundException, IOException {
    // Now return this
    return getFromFileSystem(basePath + "/data/structures/divided/msgpack" + "/"
    + pdbCode.substring(1, END_ID_FOR_MID_PDB)
    + "/" + pdbCode + ".mmtf");
  }


  /**
   * Function to get a file from the file system - full path supplied.
   *
   * @param fullPath the full path
   * @return the from file system
   * @throws FileNotFoundException the file not found exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public final Structure getFromFileSystem(final String fullPath)
      throws FileNotFoundException, IOException {
    DecodeStructure ds = new DecodeStructure();
    BioJavaStructureDecoder bjs = new BioJavaStructureDecoder();
    // Get these as an inputstream
    byte[] b = deflateGzip(IOUtils.toByteArray(
        new FileInputStream(new File(fullPath))));
    // Now get the actual structure
    ds.getStructFromByteArray(b, bjs, pp);
    return bjs.getStructure();
  }

  /**
   * Deflate a gzip byte array.
   *
   * @param inputBytes -> gzip compressed byte
   * array
   * @return A deflated byte array
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private byte[] deflateGzip(final byte[] inputBytes) throws IOException {

    ByteArrayInputStream bis = new ByteArrayInputStream(inputBytes);
    GZIPInputStream gis = new GZIPInputStream(bis);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // Make a buffer
    byte[] tmp = new byte[BYTE_BUFFER_CHUNK_SIZE];
    try {
      while (gis.available() == 1) {
        int size = gis.read(tmp);
        baos.write(tmp, 0, size);
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    } finally {
      try {
        if (baos != null) {
          baos.close();
        }
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }
    // Get the bytes
    return baos.toByteArray();
  }

}
