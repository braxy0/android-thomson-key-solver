package org.exobel.routerkeygen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class PersitentData implements Serializable{
	private static final long serialVersionUID = 1L;
	List<String> list_key;
	List<WifiNetwork> list;
	String router;
	public PersitentData( List<WifiNetwork> list , String router , List<String> list_key){
		if ( list == null )
			this.list = new ArrayList<WifiNetwork>();
		else
			this.list = list;
		if ( list_key == null )
			this.list_key = new ArrayList<String>();
		else
			this.list_key = list_key;
		if ( router == null)
			this.router = "";
		else
			this.router = router;
	}
}