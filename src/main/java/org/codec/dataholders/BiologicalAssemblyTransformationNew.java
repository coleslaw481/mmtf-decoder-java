package org.codec.dataholders;


import org.biojava.nbio.structure.xtal.CrystalCell;
import org.biojava.nbio.structure.xtal.CrystalTransform;
import org.biojava.nbio.core.util.PrettyXMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.vecmath.Point3d;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The transformation needed for generation of biological assemblies 
 * from the contents of a PDB/mmCIF file. It contains both the actual
 * transformation (rotation+translation) and the chain identifier to 
 * which it should be applied.
 * 
 * @author Peter Rose
 * @author Andreas Prlic
 * @author rickb
 * @author duarte_j
 * @see CrystalTransform
 */
public class BiologicalAssemblyTransformationNew implements Cloneable, Serializable {


	private String id;
	private String chainId;	
	private double[] transformation;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	public double[] getTransformation() {
		return transformation;
	}
	public void setTransformation(double[] transformation) {
		this.transformation = transformation;
	}


}