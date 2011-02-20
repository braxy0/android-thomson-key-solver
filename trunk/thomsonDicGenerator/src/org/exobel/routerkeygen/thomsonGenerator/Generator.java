package org.exobel.routerkeygen.thomsonGenerator;

public class Generator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i = 0; i < args.length; ++i)
        {
            if(args[i].equalsIgnoreCase("webdic"))
            {
                Stage1.main(args);
        		Stage2.main(args);
                StageWebdic.main(args);
                System.out.println("Thomson WEBDictionary created. It is on the folder named webdic.");
            	System.out.println("Any other file *.dat in the folder can be eliminated.");
                return;
            }
        }
		Stage1.main(args);
		Stage2.main(args);
		Stage3.main(args);
		Stage4.main(args);
    	System.out.println("Thomson Dictionary created. It's a file named RouterKeygen.dic.");
    	System.out.println("Any other file *.dat in the folder can be eliminated.");
	}

}
