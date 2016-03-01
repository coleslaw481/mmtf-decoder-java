package org.codec.decoder;

public class ParsingParams {
	
	/** 
	 * Whether to use internal chain ids or not
	 */
	private boolean parseInternal;

	public ParsingParams(){
		parseInternal = false;
	}
	
	public boolean isParseInternal() {
		return parseInternal;
	}

	public void setParseInternal(boolean parseInternal) {
		this.parseInternal = parseInternal;
	}
	
	
	
}
