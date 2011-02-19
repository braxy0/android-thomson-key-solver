package org.exobel.routerkeygen;

import java.io.Serializable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


class AliceHandle extends DefaultHandler implements Serializable{
	private static final long serialVersionUID = -1867841551140131246L;
	String alice;
	boolean supported;
	int [] magic;
	String serial;
	String mac;
	public AliceHandle(String alice){
		super();
		this.alice = alice;
		this.supported = false;
		this.magic = new int [2];
	} 
	public void startElement(String uri, String localName,
	        String qName, Attributes attributes){
		if ( alice.equalsIgnoreCase(localName) )
		{
			serial = attributes.getValue("sn");
			mac = attributes.getValue("mac");
			magic[0] = Integer.parseInt(attributes.getValue("q"));
			magic[1] = Integer.parseInt(attributes.getValue("k"));
			supported = true;
		}
	}
	
	public void endElement( String namespaceURI,
	              String localName,
	              String qName ) throws SAXException {}
	
	public void characters( char[] ch, int start, int length )
	              throws SAXException {}
}

