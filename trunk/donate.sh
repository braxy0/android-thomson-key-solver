#!/bin/bash

PATCH_FILE=donate.patch
FOLDER=android/jni/include
if [ "$1" == "-r" ] || [ "$1" == "-R" ]
then
	svn revert -R android/  2>&1
	rm $FOLDER/org_exobel_routerkeygen_donate_NativeThomson.h   2>&1
	rm $FOLDER/org_exobel_routerkeygen_donate_ThomsonKeygen.h   2>&1
	echo "Changes reversed" 
	exit
fi

if [ -f "$PATCH_FILE" ]
then
	patch -f -p0 < $PATCH_FILE  2>&1
else
	echo "A patch file must be present!" 
	exit
fi

mv $FOLDER/org_exobel_routerkeygen_NativeThomson.h $FOLDER/org_exobel_routerkeygen_donate_NativeThomson.h  2>&1
mv $FOLDER/org_exobel_routerkeygen_ThomsonKeygen.h $FOLDER/org_exobel_routerkeygen_donate_ThomsonKeygen.h  2>&1
export NDK_PROJECT_PATH=/home/ruka/Projectos/RouterKeygen/android/
echo "Don't forget to invoke ndk-build now." 

