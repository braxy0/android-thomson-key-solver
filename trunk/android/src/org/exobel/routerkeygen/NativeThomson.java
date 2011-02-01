package org.exobel.routerkeygen;

public class NativeThomson {
    	  static {
    		  System.load("/data/data/org.exobel.routerkeygen/libthomson.so");
    		  }
    		  
    		  /** 
    		   * Adds two integers, returning their sum
    		   */
    		  public native String thomson( byte [] essid );
    		  

}
