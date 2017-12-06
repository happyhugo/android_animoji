#include <jni.h>
#include <android/log.h>
#include <cstring>

#include "agora/IAgoraRtcEngine.h"
#include "agora/IAgoraMediaEngine.h"

#include "video_preprocessing_plugin_jni.h"

#define  LOG_TAG    "FaceTracker"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

JavaVM* javaVM;
JNIEnv* env;

jbyte* g_buffer;
int g_buffer_length;

jclass renderClass;
jmethodID fuRenderToNV21ImageMethod;
jmethodID createBufferMethod;

#include "SoundTouch/SoundTouch.h"
using namespace soundtouch;
static SoundTouch ptr;

class AgoraVideoFrameObserver : public agora::media::IVideoFrameObserver
{
public:
    virtual bool onCaptureVideoFrame(VideoFrame& videoFrame) override
    {
//        int width = videoFrame.width;
//        int height = videoFrame.height;
//
//        memset(videoFrame.uBuffer, 128, videoFrame.uStride * height / 2);
//        memset(videoFrame.vBuffer, 128, videoFrame.vStride * height / 2);
//
//        return true;
        writeLog("start to onCaptureVideoFrame,rotation:");
        javaVM->AttachCurrentThread(&env, NULL);

        jsize len = videoFrame.width * videoFrame.height * 3 / 2;
        if(g_buffer_length == 0 || g_buffer_length != len){
            g_buffer_length = len;

            createBufferMethod = env->GetStaticMethodID(renderClass, "createBuffer", "(I)Ljava/nio/ByteBuffer;");
            fuRenderToNV21ImageMethod = env->GetStaticMethodID(renderClass, "renderYuvFrame",
                                                               "(III)I");

            jobject buf = env->CallStaticObjectMethod(renderClass, createBufferMethod,g_buffer_length);
            g_buffer = (jbyte*)env->GetDirectBufferAddress(buf);
        }

        //NV21 颜色空间排列 ：YYYYYYYY VUVU
        int yLength = videoFrame.width * videoFrame.height;
        memcpy(g_buffer,videoFrame.yBuffer,yLength);

        int i = yLength;
        int uLength = yLength / 4;
        for (int j = 0; j < uLength; i += 2, j++) {
            g_buffer[i] = ((jbyte *) videoFrame.vBuffer)[j];
            g_buffer[i + 1] = ((jbyte *) videoFrame.uBuffer)[j];
        }

        writeLog("start to fuRenderToNV21ImageMethod");
        int ret = env->CallStaticIntMethod(renderClass, fuRenderToNV21ImageMethod,
                                           videoFrame.width, videoFrame.height,
                                           videoFrame.rotation);

        if(ret < 0){
            writeLog("do not render frame");
            javaVM->DetachCurrentThread();
            return true;
        }

        writeLog("start to i420 => Nv21");

        for (i = 0; i < yLength; i++) {
            ((jbyte *) videoFrame.yBuffer)[i] = g_buffer[i];
        }
        for (int j = 0; j < uLength; i += 2, j++) {
            ((jbyte *) videoFrame.vBuffer)[j] = g_buffer[i];
            ((jbyte *) videoFrame.uBuffer)[j] = g_buffer[i + 1];
        }
        //i420 => Nv21
//        memcpy(videoFrame.yBuffer,g_buffer,yLength);

//        int stride = videoFrame.width;
//        int outputFileHeight = videoFrame.height;
//
//        int k = 0;
//        int offset = 0;
//        // Write U
//        for (int r = outputFileHeight; r < outputFileHeight * 3 / 2; ++r) {
//            offset = r * stride;
//            for(int j = 0;j < stride / 2;j++) {
//                ((jbyte *) videoFrame.uBuffer)[k++] = g_buffer[offset + j];
//            }
//        }
//
//        k = 0;
//        // Write V
//        for (int r = outputFileHeight; r < outputFileHeight * 3 / 2; ++r) {
//            offset = r * stride + stride / 2;
//            for(int j = 0;j < stride / 2;j++) {
//                ((jbyte *) videoFrame.vBuffer)[k++] = g_buffer[offset + j];
//            }
//        }

        javaVM->DetachCurrentThread();

        writeLog("end to onCaptureVideoFrame");
        return true;
	}

    virtual bool onRenderVideoFrame(unsigned int uid, VideoFrame& videoFrame) override
    {
        return true;
    }

    void writeLog(char *text,...){
//        LOGE("%s",text);
    }
};

class AgoraAudioFrameObserver : public agora::media::IAudioFrameObserver
{
public:
    virtual bool onRecordAudioFrame(AudioFrame& audioFrame) override
    {
        return true;
    }


    virtual bool onPlaybackAudioFrame(AudioFrame& audioFrame) override
    {
//        short* p = (short*)audioFrame.buffer;
//        for(int i=0,j=0;j<320; i=i+2,j=j+4) {
//            *(*p+i) = *(*p+j);
//            *(*p+i+1) = *(*p+j+1);
//        }
//        __android_log_print(ANDROID_LOG_ERROR, "plugin", "plugin onRecordAudioFrame %d,%d    ,  %d, %d , %d, %d, %d, %d",p[30]
//                            ,audioFrame.samplesPerSec,audioFrame.samples,audioFrame.channels,audioFrame.bytesPerSample,audioFrame.renderTimeMs);

//        for(int i=0;i<320;i++){
//            *(*p+i) = 0;
//        }
//        __android_log_print(ANDROID_LOG_ERROR, "plugin", "plugin onPlaybackAudioFrame");

//        short* p = (short*)audioFrame.buffer;
//        int a = 0;
//        for(int i=0,j=0;j<audioFrame.samples; i=i+2,j=j+4) {
//            p[i] = p[j];
//            p[i+1] = p[j+1];
//            a = i;
//        }
//        audioFrame.samples = a+2;
        ptr.putSamples((const SAMPLETYPE *)(audioFrame.buffer), audioFrame.samples);
        int nSamples = ptr.receiveSamples((SAMPLETYPE *)audioFrame.buffer, audioFrame.samples);
        audioFrame.samples = nSamples;
//      __android_log_print(ANDROID_LOG_ERROR, "plugin", "plugin samples %d", nSamples);
        return true;
    }
    virtual bool onMixedAudioFrame(AudioFrame& audioFrame) override
    {
        return true;
    }
    virtual bool onPlaybackAudioFrameBeforeMixing(unsigned int uid, AudioFrame& audioFrame)  override
    {
        return true;
    }
};

static AgoraAudioFrameObserver s_audioFrameObserver;
static AgoraVideoFrameObserver s_videoFrameObserver;
static agora::rtc::IRtcEngine* rtcEngine = NULL;

#ifdef __cplusplus
extern "C" {
#endif

int __attribute__((visibility("default"))) loadAgoraRtcEnginePlugin(agora::rtc::IRtcEngine* engine)
{
    __android_log_print(ANDROID_LOG_ERROR, "plugin", "plugin loadAgoraRtcEnginePlugin");
    rtcEngine = engine;
    return 0;
}

void __attribute__((visibility("default"))) unloadAgoraRtcEnginePlugin(agora::rtc::IRtcEngine* engine)
{
    __android_log_print(ANDROID_LOG_ERROR, "plugin", "plugin unloadAgoraRtcEnginePlugin");
    rtcEngine = NULL;
}

JNIEXPORT void JNICALL Java_io_agora_propeller_preprocessing_VideoPreProcessing_enablePreProcessing
  (JNIEnv *env, jobject obj, jboolean enable)
{
    if (!rtcEngine)
        return;
    agora::util::AutoPtr<agora::media::IMediaEngine> mediaEngine;
    mediaEngine.queryInterface(rtcEngine, agora::rtc::AGORA_IID_MEDIA_ENGINE);
    if (mediaEngine) {
        if (enable) {
            mediaEngine->registerVideoFrameObserver(&s_videoFrameObserver);
        } else {
            mediaEngine->registerVideoFrameObserver(NULL);
        }
    }


    env->GetJavaVM(&javaVM);

    renderClass = env->FindClass("io/agora/openvcall/tracker/AGRender");
    renderClass = (jclass) env->NewGlobalRef(renderClass);
}

JNIEXPORT void JNICALL Java_io_agora_propeller_preprocessing_VideoPreProcessing_enablePreAudioProcessing
        (JNIEnv *env, jobject obj, jboolean enable)
{
    if (!rtcEngine)
        return;
    agora::util::AutoPtr<agora::media::IMediaEngine> mediaEngine;
    mediaEngine.queryInterface(rtcEngine, agora::rtc::AGORA_IID_MEDIA_ENGINE);
    if (mediaEngine) {
        if (enable) {
            __android_log_print(ANDROID_LOG_ERROR, "plugin", "plugin enable true");
            ptr.setPitch(1.7);
            ptr.setChannels(1);
            ptr.setSampleRate(32000);
            mediaEngine->registerAudioFrameObserver(&s_audioFrameObserver);
        } else {
            __android_log_print(ANDROID_LOG_ERROR, "plugin", "plugin enable false");
            mediaEngine->registerAudioFrameObserver(NULL);
        }
    }
}

#ifdef __cplusplus
}
#endif
