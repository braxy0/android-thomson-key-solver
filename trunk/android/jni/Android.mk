LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := hashword
LOCAL_SRC_FILES := hashword.c

include $(BUILD_SHARED_LIBRARY)