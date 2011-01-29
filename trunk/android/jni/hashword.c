#include "org_exobel_routerkeygen_Hash.h"
#include <stdint.h>

#define rot(x,k) (((x)<<(k)) | ((x)>>(32-(k))))
#define final(a,b,c) \
{ \
  c ^= b; c -= rot(b,14); \
  a ^= c; a -= rot(c,11); \
  b ^= a; b -= rot(a,25); \
  c ^= b; c -= rot(b,16); \
  a ^= c; a -= rot(c,4);  \
  b ^= a; b -= rot(a,14); \
  c ^= b; c -= rot(b,24); \
}
#define mix(a,b,c) \
{ \
  a -= c;  a ^= rot(c, 4);  c += b; \
  b -= a;  b ^= rot(a, 6);  a += c; \
  c -= b;  c ^= rot(b, 8);  b += a; \
  a -= c;  a ^= rot(c,16);  c += b; \
  b -= a;  b ^= rot(a,19);  a += c; \
  c -= b;  c ^= rot(b, 4);  b += a; \
}
JNIEXPORT jstring JNICALL Java_org_exobel_routerkeygen_Hash_hello
  (JNIEnv * env, jobject obj) {
		return (*env)->NewStringUTF(env, "Hello World!");
}

JNIEXPORT jint JNICALL Java_org_exobel_routerkeygen_Hash_hashword
  (JNIEnv * env, jobject njobj, jintArray key , jint length, jint initval){
	  unsigned int a,b,c;
	  unsigned int seed = initval;
	  int i = 0;
	  jint *k_native= (*env)->GetIntArrayElements(env, key, 0);
	  unsigned int k[64];
	  for ( i = 0 ; i < 64 ; ++i )
	  {
		  k[i] = k_native[i];
	  }
	  /* Set up the internal state */
	  a = b = c = 0xdeadbeef + (((uint32_t)length)<<2) + seed;

	  /*------------------------------------------------- handle most of the key */
	  i = 0;
	  while (length > 3)
	  {
	    a += k[i + 0];
	    b += k[i + 1];
	    c += k[i + 2];
	    mix(a,b,c);
	    length -= 3;
	    i += 3;
	  }

	  /*------------------------------------------- handle the last 3 uint32_t's */
	  switch(length)                     /* all the case statements fall through */
	  {
	  case 3 : c+=k[2];
	  case 2 : b+=k[1];
	  case 1 : a+=k[0];
	    final(a,b,c);
	  case 0:     /* case 0: nothing left to add */
	    break;
	  }
	  /*------------------------------------------------------ report the result */
	  (*env)->ReleaseIntArrayElements(env, key, k, 0);
	  return c;
}

