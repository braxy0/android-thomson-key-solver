LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := thomson
LOCAL_SRC_FILES := sha1.c thomson.c

include $(BUILD_SHARED_LIBRARY)
