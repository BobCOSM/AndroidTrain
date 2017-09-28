LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
APP_ABI := armeabi armeabi-v7a arm64-v8a
LOCAL_MODULE    := TrainService
LOCAL_SRC_FILES := TrainService.cpp

include $(BUILD_SHARED_LIBRARY)
