package org.jboss.tools.teiid.reddeer;

public enum ChildType {
	
	XML_DOCUMENT("XML Document"),
	NAME_SPACE("Namespace Declaration"),
	SEQUENCE("sequence"),
	ELEMENT("Element"),
	TABLE("Table..."),
	OPERATION("Operation"),
	INPUT("Input"),
	OUTPUT("Output"),
	PROCEDURE("Procedure...");
	
	private String text;

	private ChildType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
