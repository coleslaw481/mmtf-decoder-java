package org.codec.dataholders;

/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */

import java.util.List;

/**
 * Representation of a Biological Assembly annotation as provided by the PDB.
 * Contains all the information required to build the Biological Assembly from 
 * the asymmetric unit.
 * Note that the PDB allows for 1 or more Biological Assemblies for a given entry. They
 * are identified by the id field.
 * 
 * Modifeid for message pack
 * 
 * @author duarte_j
 * @author anthony
 */
public class BioAssemblyInfoNew {
	
	private int id;
	private List<BiologicalAssemblyTransformationNew> transforms;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<BiologicalAssemblyTransformationNew> getTransforms() {
		return transforms;
	}
	public void setTransforms(List<BiologicalAssemblyTransformationNew> transforms) {
		this.transforms = transforms;
	}
	public int getMacromolecularSize() {
		return macromolecularSize;
	}
	public void setMacromolecularSize(int macromolecularSize) {
		this.macromolecularSize = macromolecularSize;
	}
	private int macromolecularSize;

}

