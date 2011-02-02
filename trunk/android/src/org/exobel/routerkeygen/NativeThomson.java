package org.exobel.routerkeygen;

public class NativeThomson {
    	  static {
    		  System.loadLibrary("thomson");
    		  }
    		  
    		  /** 
    		   * Native processing without a dictionary.
    		   */
    		  public static native String[] thomson( byte [] essid );
    		  

}
