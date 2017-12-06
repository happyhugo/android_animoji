LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := video_preprocessing_plugin_jni.cpp \
                   ./SoundTouch/AAFilter.cpp  ./SoundTouch/FIFOSampleBuffer.cpp \
                   ./SoundTouch/FIRFilter.cpp ./SoundTouch/cpu_detect_x86.cpp \
                   ./SoundTouch/sse_optimized.cpp \
                   ./SoundTouch/RateTransposer.cpp ./SoundTouch/SoundTouch.cpp \
                   ./SoundTouch/InterpolateCubic.cpp ./SoundTouch/InterpolateLinear.cpp \
                   ./SoundTouch/InterpolateShannon.cpp ./SoundTouch/TDStretch.cpp \
                   ./SoundTouch/BPMDetect.cpp ./SoundTouch/PeakFinder.cpp

LOCAL_LDLIBS := -ldl -llog  -landroid

LOCAL_MODULE := apm-plugin-video-preprocessing

include $(BUILD_SHARED_LIBRARY)
