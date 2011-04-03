#!/bin/bash

PATCH_FILE=donate.patch
FOLDER=android/jni/include
if [ "$1" == "-r" ]
then
	svn revert -R android/
	rm $FOLDER/org_exobel_routerkeygen_donate_NativeThomson.h 
	rm $FOLDER/org_exobel_routerkeygen_donate_ThomsonKeygen.h 
	echo "Changes reversed" 
	exit
fi

if [ -f "$PATCH_FILE" ]
then
	patch -p0 < $PATCH_FILE
else
	echo "A patch file must be present!" 
	exit
fi

mv $FOLDER/org_exobel_routerkeygen_NativeThomson.h $FOLDER/org_exobel_routerkeygen_donate_NativeThomson.h 
mv $FOLDER/org_exobel_routerkeygen_ThomsonKeygen.h $FOLDER/org_exobel_routerkeygen_donate_ThomsonKeygen.h 
export NDK_PROJECT_PATH=/home/ruka/Projectos/RouterKeygen/android/
echo "Don't forget to invoke ndk-build now." 

