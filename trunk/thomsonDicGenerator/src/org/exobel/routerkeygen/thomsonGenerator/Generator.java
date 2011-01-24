package org.exobel.routerkeygen.thomsonGenerator;

public class Generator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Stage1.main(args);
		Stage2.main(args);
		Stage3.main(args);
		Stage4.main(args);
    	System.out.println("Thomson Dictionary created. It's a file named thomson.dic.");
    	System.out.println("Any other file *.dat in the folder can be eliminated.");
	}

}
