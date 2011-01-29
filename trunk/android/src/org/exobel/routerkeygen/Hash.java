package org.exobel.routerkeygen;

public class Hash {

	
	 static {
		    System.loadLibrary("hashword");
		  }
		  
		  /** 
		   * Hash
		   */
		  public native int hashword(int[] key,int length, int seed);
		  
		  /**
		   * Returns Hello World string
		   */
		  public native String hello();
}
