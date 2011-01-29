package org.exobel.routerkeygen;

public class TelseyKeygen extends KeygenThread {

	public TelseyKeygen(RouterKeygen par) {
		super(par);
	}

	int [] scrambler(String mac){
		int []vector = new int [64];
		byte [] macValue = new byte[6];
		for (int i = 0; i < 12; i += 2)
			macValue[i / 2] = (byte) ((Character.digit(mac.charAt(i), 16) << 4)
					+ Character.digit(mac.charAt(i + 1), 16));

		vector[0] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
				   ( ( 0xFF & macValue[0] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[1] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
				   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[2] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[3] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[4] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		           ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[5] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[5] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[6] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		   		   ( ( 0xFF & macValue[0] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[7] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[8] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[9] =0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[10]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[11]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[12]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[0] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[13]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[14]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[15]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[16]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		   	  	   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[17]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[18]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[5] ) << 16 ) |
		   		   ( ( 0xFF & macValue[0] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[19]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
				   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[20]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[21]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[22]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		   		   ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[23]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[24]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[0] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[25]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[26]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[27]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[28]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[29]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[30]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[0] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[31]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[32]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[33]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[34]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[35]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[5] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[36]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[37]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[38]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[39]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[40]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[41]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[5] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[42]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[43]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[44]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[45]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[46]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
				   ( ( 0xFF & macValue[0] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[47]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[48]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[1] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[49]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[50]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[51]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		   	       ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[52]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[53]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[5] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[54]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[4] ) ));
		vector[55]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[0] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[56]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[5] ) ));
		vector[57]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[4] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[58]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[4] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[0] ) ));
		vector[59]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[2] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[5] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[60]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[3] ) << 24 )|( ( 0xFF & macValue[1] ) << 16 ) |
		   		   ( ( 0xFF & macValue[2] ) << 8 )|( 0xFF & macValue[3] ) ));
		vector[61]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[0] ) << 16 ) |
		   		   ( ( 0xFF & macValue[1] ) << 8 )|( 0xFF & macValue[2] ) ));
		vector[62]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[5] ) << 24 )|( ( 0xFF & macValue[3] ) << 16 ) |
		   		   ( ( 0xFF & macValue[4] ) << 8 )|( 0xFF & macValue[1] ) ));
		vector[63]=0xFFFFFFFF & ((int)( ( ( 0xFF & macValue[0] ) << 24 )|( ( 0xFF & macValue[2] ) << 16 ) |
		   		   ( ( 0xFF & macValue[3] ) << 8 )|( 0xFF & macValue[0] ) ));
		@SuppressWarnings("unused")
		String test = "";
		for (int x = 0; x < 64; x++) {
			test  = Integer.toHexString(vector[x]);
			vector[x] = 0xFFFFFFFF & ((int)( ( ( 0xFF & vector[x] ) << 24 )|( ( 0xFF00 & vector[x] ) << 8 ) |
			   		   ( ( 0xFF0000 & vector[x] ) >>> 8 )|( ( 0xFF000000 & vector[x] ) >>> 24) ) );
			test  = Integer.toHexString(vector[x]);

		}
		return vector;
	}	
	
	public void run(){
		Hash hash = new Hash();
		if ( router.mac.equals("") ) 
		{
			pwList.add(parent.getResources().getString(R.string.msg_nomac));
			parent.list_key =  pwList;
			parent.handler.sendEmptyMessage(1);
			return;
		}
		int [] key = scrambler("002196123456");
		int seed = 0;
		for (int x = 0; x < 64; x++) {
			seed = hash.hashword(key,x, seed);
		}
		String S1 = Integer.toHexString(seed);
		
		
		for ( int x = 0; x <64; x++) {
			    if (x <8)	
			    	key[x] <<= 3; 
			    else if ( x<16)
			    	key[x] >>>= 5; 
			    else if (x < 32 ) 
			    	key[x] >>>= 2;
			    else
			    	key[x] <<= 7;
		}
		@SuppressWarnings("unused")
		String test = "";
		for (int x = 0; x < 64; x++) {
			test  = Integer.toHexString(key[x]);
		}
		
		seed = 0;
		for (int x = 0; x < 64; x++) {
			seed = hash.hashword(key, x, seed);
		}
		String S2 =  Integer.toHexString(seed);
		pwList.add(S1.substring(S1.length() - 5) +  S2.substring(0, 5));
		parent.list_key =  pwList;
		parent.handler.sendEmptyMessage(0);
		return;
	}
	
	
}
