LOCAL_PATH := $(call my-dir)
  
include $(CLEAR_VARS) 
  
LOCAL_MODULE    := key
#LOCAL_SRC_FILES
LOCAL_SRC_FILES := key.c
#LOCAL_SRC_FILES += Add.cpp
 
include $(BUILD_SHARED_LIBRARY)