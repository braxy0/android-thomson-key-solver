#include "org_exobel_routerkeygen_NativeThomson.h"
#include <ctype.h>
#include <string.h>
#include "sha1.h"
#include "unknown.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>


JNIEXPORT jstring JNICALL Java_org_exobel_routerkeygen_NativeThomson_thomson
  (JNIEnv * env, jobject obj, jbyteArray ess )
{
    int n = sizeof(dic)/sizeof("AAA");
    jint *e_native= (*env)->GetIntArrayElements(env, ess, 0);
    uint8_t ssid[3];
    ssid[0] = e_native[0];
    ssid[1] = e_native[1];
    ssid[2] = e_native[2];
	uint8_t message_digest[20];
	SHA_CTX sha1;
	int year = 7;
	int week = 1;
	int i = 0 ;
	char input[13];
	input[0] = 'C';
	input[1] = 'P';
	input[2] = '0';
	char result [50]  ="";
//	uint8_t * ptr = (uint8_t *)malloc(3*sizeof(uint8_t));
	for( i = 0 ; i < n; ++i  )
	{
		sprintf( (&input[0]) + 6, "%02X%02X%02X" , (int)dic[i][0]
		                        , (int)dic[i][1], (int)dic[i][2] );        
//		for ( year = 4 ; year <= 9 ; ++year )
		{
		    for ( week = 1 ; week <= 52 ; ++week )
		    {
		        input[3] = '0' + year % 10 ;
		        input[4] = '0' + week / 10;
		        input[5] = '0' + week % 10;
		        SHA1_Init(&sha1);
		        SHA1_Update(&sha1 ,(const void *) input , 12 );
		        SHA1_Final(message_digest , &sha1 );
		        /*
		        * Little Endian
		        *
		        memcpy(ptr , message_digest + 19 , 1);
		        memcpy(ptr + 1, message_digest + 18 , 1);
		        memcpy(ptr + 2, message_digest + 17 , 1);*/
		        if(memcmp(&message_digest[17],&ssid[0],3) == 0){
		        sprintf( result, "%02X%02X%02X%02X%02X\n" , message_digest[0], message_digest[1] , 
                                        message_digest[2] , message_digest[3], message_digest[4] ); 
		        	return (*env)->NewStringUTF(env, result);
		        }
		    }
		}
	}
 	return (*env)->NewStringUTF(env, result);
}
