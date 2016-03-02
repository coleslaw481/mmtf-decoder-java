package org.rcsb.mmtf.dataholders;


import org.biojava.nbio.structure.xtal.CrystalTransform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The transformation needed for generation of biological assemblies 
 * from the contents of a PDB/mmCIF file. It contains both the actual
 * transformation (rotation+translation) and the chain identifier to 
 * which it should be applied.
 * 
 * Modified for message pack
 * @author Peter Rose
 * @author Andreas Prlic
 * @author rickb
 * @author duarte_j
 * @author anthony
 * @see CrystalTransform
 */
public class BiologicalAssemblyTransformationNew implements Cloneable, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8109941242652091495L;
	private String id;
	private List<String> chainId = new ArrayList<String>();	
	private double[] transformation;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public double[] getTransformation() {
		return transformation;
	}
	public void setTransformation(double[] transformation) {
		this.transformation = transformation;
	}
	public List<String> getChainId() {
		return chainId;
	}
	public void setChainId(List<String> chainId) {
		this.chainId = chainId;
	}


}