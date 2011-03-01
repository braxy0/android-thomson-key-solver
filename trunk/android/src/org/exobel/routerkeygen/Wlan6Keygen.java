package org.exobel.routerkeygen;

public class Wlan6Keygen extends KeygenThread {

	public Wlan6Keygen(RouterKeygen par) {
		super(par);
	}
/*	  private String[] generardic1(String s, String s1)
	    {
	        String as[];
	        char [] ESSID = new char [10];
	        char []BSSID= new char [10];
	        int k;
	        int j1;
	        int k1;
	        int l1;
	        int j2;
	        int k2;
	        int l2;
	        int i3;
	        int j3;
	        String s2 = s;
	        int i = 0;
	        char c = s2.charAt(i);
	        int j = 87;
	        char ac6[];
	        String s9;
	        byte byte6;
	        char c7;
	        char ac7[];
	        String s10;
	        byte byte7;
	        char c8;
	        int l;
	        byte byte8;
	        int i1;
	        byte byte9;
	        int i2;
	        int k3;
	        byte byte10;
	        if(c == 'W')
	        {
	            char ac[] = ESSID;
	            ac[4] = s.charAt(4);
	            char ac1[] = ESSID;
	            ac1[5] = s.charAt(5);
	            char ac2[] = ESSID;
	            ac2[6] = s.charAt(6);
	            char ac3[] = ESSID;
	            ac3[7] = s.charAt(7);
	            char ac4[] = ESSID;
	            ac4[8] = s.charAt(8);
	            char ac5[] = ESSID;
	            ac5[9] = s.charAt(9);
	        } else
	        {
	            char ac8[] = ESSID;
	            String s11 = s;
	            byte byte11 = 5;
	            char c9 = s11.charAt(byte11);
	            ac8[4] = c9;
	            char ac9[] = ESSID;
	            String s12 = s;
	            byte byte12 = 6;
	            char c10 = s12.charAt(byte12);
	            ac9[5] = c10;
	            char ac10[] = ESSID;
	            String s13 = s;
	            byte byte13 = 7;
	            char c11 = s13.charAt(byte13);
	            ac10[6] = c11;
	            char ac11[] = ESSID;
	            String s14 = s;
	            byte byte14 = 8;
	            char c12 = s14.charAt(byte14);
	            ac11[7] = c12;
	            char ac12[] = ESSID;
	            String s15 = s;
	            byte byte15 = 9;
	            char c13 = s15.charAt(byte15);
	            ac12[8] = c13;
	            char ac13[] = ESSID;
	            String s16 = s;
	            byte byte16 = 10;
	            char c14 = s16.charAt(byte16);
	            ac13[9] = c14;
	        }
	        ac6 = BSSID;
	        ac6[15] = s1.charAt(15);
	        ac7 = BSSID;
	        ac7[16] = s1.charAt(16);
	        as = new String[10];
	        as[0] = "AAAAAAAAAAAAA";
	        as[1] = "BBBBBBBBBBBBB";
	        as[2] = "CCCCCCCCCCCCC";
	        as[3] = "DDDDDDDDDDDDD";
	        as[4] = "EEEEEEEEEEEEE";
	        as[5] = "FFFFFFFFFFFFF";
	        as[6] = "GGGGGGGGGGGGG";
	        as[7] = "HHHHHHHHHHHHH";
	        as[8] = "IIIIIIIIIIIII";
	        as[9] = "JJJJJJJJJJJJJ";
	        k = 4;
	_L5:
	        l = k;
	        if(l <= 9) goto _L2; else goto _L1
	_L1:
	        k = 15;
	_L6:
	        i1 = k;
	        if(i1 <= 16) goto _L4; else goto _L3
	_L3:
	        j1 = ESSID[7] & 0xf;
	        k1 = ESSID[8] & 0xf;
	        l1 = ESSID[9] & 0xf;
	        i2 = ESSID[4] & 0xf;
	        j2 = ESSID[5] & 0xf;
	        k2 = ESSID[6] & 0xf;
	        l2 = BSSID[15] & 0xf;
	        i3 = BSSID[16] & 0xf;
	        j3 = 0;
	_L7:
	        k3 = j3;
	        byte10 = 9;
	        if(k3 > byte10)
	            return as;
	        break MISSING_BLOCK_LABEL_755;
	_L2:
	        char c15 = ESSID[k];
	        byte byte17 = 65;
	        if(c15 >= byte17)
	        {
	            char ac14[] = ESSID;
	            char c16 = (char)(ac14[k] - 55);
	            ac14[k] = c16;
	        }
	        k++;
	          goto _L5;
	_L4:
	        char c17 = BSSID[k];
	        byte byte18 = 65;
	        if(c17 >= byte18)
	        {
	            char ac15[] = BSSID;
	            char c18 = (char)(ac15[k] - 55);
	            ac15[k] = c18;
	        }
	        k++;
	          goto _L6
	        int l3 = j3 + j1 + l2 + i3;
	        int i4 = j2 + k2 + k1 + l1;
	        int j4 = l3 ^ l1;
	        int k4 = l3 ^ k1;
	        int l4 = l3 ^ j1;
	        int i5 = i4 ^ k2;
	        int j5 = i4 ^ l2;
	        int k5 = i4 ^ i3;
	        int l5 = l2 ^ l1;
	        int i6 = i3 ^ k1;
	        int j6 = l3 ^ i4;
	        int k6 = j4 ^ i6;
	        int l6 = j5 ^ k5;
	        int i7 = i5 ^ l4;
	        int j7 = j6 ^ k4;
	        Object aobj[] = new Object[13];
	        Integer integer = Integer.valueOf(j7 & 0xf);
	        aobj[0] = integer;
	        Integer integer1 = Integer.valueOf(j4 & 0xf);
	        aobj[1] = integer1;
	        Integer integer2 = Integer.valueOf(i5 & 0xf);
	        aobj[2] = integer2;
	        Integer integer3 = Integer.valueOf(l5 & 0xf);
	        aobj[3] = integer3;
	        Integer integer4 = Integer.valueOf(k6 & 0xf);
	        aobj[4] = integer4;
	        Integer integer5 = Integer.valueOf(k4 & 0xf);
	        aobj[5] = integer5;
	        Integer integer6 = Integer.valueOf(j5 & 0xf);
	        aobj[6] = integer6;
	        Integer integer7 = Integer.valueOf(i6 & 0xf);
	        aobj[7] = integer7;
	        Integer integer8 = Integer.valueOf(l6 & 0xf);
	        aobj[8] = integer8;
	        Integer integer9 = Integer.valueOf(l4 & 0xf);
	        aobj[9] = integer9;
	        Integer integer10 = Integer.valueOf(k5 & 0xf);
	        aobj[10] = integer10;
	        Integer integer11 = Integer.valueOf(j6 & 0xf);
	        aobj[11] = integer11;
	        Integer integer12 = Integer.valueOf(i7 & 0xf);
	        aobj[12] = integer12;
	        String s17 = String.format("%X%X%X%X%X%X%X%X%X%X%X%X%X", aobj);
	        as[j3] = s17;
	        j3++;
	          goto _L7
	    }

	
	private String[] generardic(String paramString1, String paramString2)
	  {
	    String str1 = paramString1;
	    int i = 0;
	    int j = str1.charAt(i);
	    int k = 87;
	    String[] arrayOfString;
	    int i15;
	    label326: 
	    	label345: int i20;
	    int i21;
	    int i22;
	    int i24;
	    int i25;
	    int i26;
	    int i27;
	    int i28;
	    if (j == k)
	    {
	      char[] arrayOfChar1 = this.ESSID;
	      String str2 = paramString1;
	      int m = 4;
	      int n = str2.charAt(m);
	      arrayOfChar1[4] = (char) n;
	      char[] arrayOfChar2 = this.ESSID;
	      String str3 = paramString1;
	      int i1 = 5;
	      int i2 = str3.charAt(i1);
	      arrayOfChar2[5] = i2;
	      char[] arrayOfChar3 = this.ESSID;
	      String str4 = paramString1;
	      int i3 = 6;
	      int i4 = str4.charAt(i3);
	      arrayOfChar3[6] = i4;
	      char[] arrayOfChar4 = this.ESSID;
	      String str5 = paramString1;
	      int i5 = 7;
	      int i6 = str5.charAt(i5);
	      arrayOfChar4[7] = i6;
	      char[] arrayOfChar5 = this.ESSID;
	      String str6 = paramString1;
	      int i7 = 8;
	      int i8 = str6.charAt(i7);
	      arrayOfChar5[8] = i8;
	      char[] arrayOfChar6 = this.ESSID;
	      String str7 = paramString1;
	      int i9 = 9;
	      int i10 = str7.charAt(i9);
	      arrayOfChar6[9] = i10;
	      char[] arrayOfChar7 = this.BSSID;
	      String str8 = paramString2;
	      int i11 = 15;
	      int i12 = str8.charAt(i11);
	      arrayOfChar7[15] = i12;
	      char[] arrayOfChar8 = this.BSSID;
	      String str9 = paramString2;
	      int i13 = 16;
	      int i14 = str9.charAt(i13);
	      arrayOfChar8[16] = i14;
	      arrayOfString = new String[10];
	      arrayOfString[0] = "AAAAAAAAAAAAA";
	      arrayOfString[1] = "BBBBBBBBBBBBB";
	      arrayOfString[2] = "CCCCCCCCCCCCC";
	      arrayOfString[3] = "DDDDDDDDDDDDD";
	      arrayOfString[4] = "EEEEEEEEEEEEE";
	      arrayOfString[5] = "FFFFFFFFFFFFF";
	      arrayOfString[6] = "GGGGGGGGGGGGG";
	      arrayOfString[7] = "HHHHHHHHHHHHH";
	      arrayOfString[8] = "IIIIIIIIIIIII";
	      arrayOfString[9] = "JJJJJJJJJJJJJ";
	      i15 = 4;
	      int i16 = i15;
	      int i17 = 9;
	      if (i16 <= i17)
	        break label649;
	      i15 = 15;
	      int i18 = i15;
	      int i19 = 16;
	      if (i18 <= i19)
	        break label702;
	      i20 = this.ESSID[7] & 0xF;
	      i21 = this.ESSID[8] & 0xF;
	      i22 = this.ESSID[9] & 0xF;
	      int i23 = this.ESSID[4] & 0xF;
	      i24 = this.ESSID[5] & 0xF;
	      i25 = this.ESSID[6] & 0xF;
	      i26 = this.BSSID[15] & 0xF;
	      i27 = this.BSSID[16] & 0xF;
	      i28 = 0;
	    }
	    while (true)
	    {
	      int i29 = i28;
	      int i30 = 9;
	      if (i29 > i30)
	      {
	        return arrayOfString;
	        char[] arrayOfChar9 = this.ESSID;
	        String str10 = paramString1;
	        int i31 = 5;
	        int i32 = str10.charAt(i31);
	        arrayOfChar9[4] = i32;
	        char[] arrayOfChar10 = this.ESSID;
	        String str11 = paramString1;
	        int i33 = 6;
	        int i34 = str11.charAt(i33);
	        arrayOfChar10[5] = i34;
	        char[] arrayOfChar11 = this.ESSID;
	        String str12 = paramString1;
	        int i35 = 7;
	        int i36 = str12.charAt(i35);
	        arrayOfChar11[6] = i36;
	        char[] arrayOfChar12 = this.ESSID;
	        String str13 = paramString1;
	        int i37 = 8;
	        int i38 = str13.charAt(i37);
	        arrayOfChar12[7] = i38;
	        char[] arrayOfChar13 = this.ESSID;
	        String str14 = paramString1;
	        int i39 = 9;
	        int i40 = str14.charAt(i39);
	        arrayOfChar13[8] = i40;
	        char[] arrayOfChar14 = this.ESSID;
	        String str15 = paramString1;
	        int i41 = 10;
	        int i42 = str15.charAt(i41);
	        arrayOfChar14[9] = i42;
	        break;
	        label649: int i43 = this.ESSID[i15];
	        int i44 = 65;
	        if (i43 >= i44)
	        {
	          char[] arrayOfChar15 = this.ESSID;
	          int i45 = (char)(arrayOfChar15[i15] - '7');
	          arrayOfChar15[i15] = i45;
	        }
	        i15 += 1;
	        break label326;
	        label702: int i46 = this.BSSID[i15];
	        int i47 = 65;
	        if (i46 >= i47)
	        {
	          char[] arrayOfChar16 = this.BSSID;
	          int i48 = (char)(arrayOfChar16[i15] - '7');
	          arrayOfChar16[i15] = i48;
	        }
	        i15 += 1;
	        break label345;
	      }
	      int i49 = i28 + i20 + i26 + i27;
	      int i50 = i24 + i25 + i21 + i22;
	      int i51 = i49 ^ i22;
	      int i52 = i49 ^ i21;
	      int i53 = i49 ^ i20;
	      int i54 = i50 ^ i25;
	      int i55 = i50 ^ i26;
	      int i56 = i50 ^ i27;
	      int i57 = i26 ^ i22;
	      int i58 = i27 ^ i21;
	      int i59 = i49 ^ i50;
	      int i60 = i51 ^ i58;
	      int i61 = i55 ^ i56;
	      int i62 = i54 ^ i53;
	      int i63 = i59 ^ i52;
	      Object[] arrayOfObject = new Object[13];
	      Integer localInteger1 = Integer.valueOf(i63 & 0xF);
	      arrayOfObject[0] = localInteger1;
	      Integer localInteger2 = Integer.valueOf(i51 & 0xF);
	      arrayOfObject[1] = localInteger2;
	      Integer localInteger3 = Integer.valueOf(i54 & 0xF);
	      arrayOfObject[2] = localInteger3;
	      Integer localInteger4 = Integer.valueOf(i57 & 0xF);
	      arrayOfObject[3] = localInteger4;
	      Integer localInteger5 = Integer.valueOf(i60 & 0xF);
	      arrayOfObject[4] = localInteger5;
	      Integer localInteger6 = Integer.valueOf(i52 & 0xF);
	      arrayOfObject[5] = localInteger6;
	      Integer localInteger7 = Integer.valueOf(i55 & 0xF);
	      arrayOfObject[6] = localInteger7;
	      Integer localInteger8 = Integer.valueOf(i58 & 0xF);
	      arrayOfObject[7] = localInteger8;
	      Integer localInteger9 = Integer.valueOf(i61 & 0xF);
	      arrayOfObject[8] = localInteger9;
	      Integer localInteger10 = Integer.valueOf(i53 & 0xF);
	      arrayOfObject[9] = localInteger10;
	      Integer localInteger11 = Integer.valueOf(i56 & 0xF);
	      arrayOfObject[10] = localInteger11;
	      Integer localInteger12 = Integer.valueOf(i59 & 0xF);
	      arrayOfObject[11] = localInteger12;
	      Integer localInteger13 = Integer.valueOf(i62 & 0xF);
	      arrayOfObject[12] = localInteger13;
	      String str16 = String.format("%X%X%X%X%X%X%X%X%X%X%X%X%X", arrayOfObject);
	      arrayOfString[i28] = str16;
	      i28 += 1;
	    }
	  }
*/
}
