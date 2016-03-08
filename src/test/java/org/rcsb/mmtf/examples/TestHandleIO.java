package org.rcsb.mmtf.examples;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertArrayEquals;

public class TestHandleIO {

  private HandleIO handleIo;
  
   public TestHandleIO() {
    // Generate this utility class
     handleIo = new HandleIO();
  }
  
  
  /**
   * Basic test of being able to reach the server and get a byte array back
   */
   @Test
  public void testGetFileFromServer() {
    
    // Get the base url
    byte[] inputArr = handleIo.getFromUrl("4cup");
    assertNotEquals(inputArr, null);
    assertNotEquals(inputArr.length, 1);
    assertNotEquals(inputArr.length, 0);
    // Get the base url
    byte[] inputArrTwo = handleIo.getFromUrl("4cup");   
    assertArrayEquals(inputArr, inputArrTwo);
    // What if we give an nonsense PDB code
    byte[] inputArrThree = handleIo.getFromUrl("12345");  
    assertEquals(inputArrThree, null);
  }
  
  /**
   * Test whether we can read write the files where we want
   */
  @Test
  public void testReadWriteFiles() {
    // The input code
    String inputCode = "4cup";
    // If we don't set where the cache is
    assertEquals(handleIo.getFromFile(inputCode), null);
    // If we set it as the tmp directory
    String tmpDirPath  = System.getProperty("java.io.tmpdir");
    System.setProperty("PDB_CACHE_DIR", tmpDirPath);
    byte[] urlData = handleIo.getFromUrlOrFile(inputCode);
    byte[] fileData = handleIo.getFromFile(inputCode);
    // First check neither are null
    assertNotEquals(urlData, null);
    assertNotEquals(fileData, null);
    // Now check they are the same
    assertArrayEquals(urlData, fileData);
  }
  
  /**
   * Test whether we can update files from the server and the updated file is created.
   * Not implemented yet. As functionality isn't there.
   */
  @Test
  public void testUpdateFiles() { 
    
  }
  
}
