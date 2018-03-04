#include <jni.h>
#include <android/log.h>
#include <cstring>

#include "agora/IAgoraRtcEngine.h"
#include "agora/IAgoraMediaEngine.h"

#include "video_preprocessing_plugin_jni.h"

#include "SoundTouch/SoundTouch.h"
using namespace soundtouch;
static SoundTouch ptr;


class AgoraAudioFrameObserver : public agora::media::IAudioFrameObserver
{
public:
    virtual bool onRecordAudioFrame(AudioFrame& audioFrame) override
    {
        return true;
    }


    virtual bool onPlaybackAudioFrame(AudioFrame& audioFrame) override
    {
        ptr.putSamples((const SAMPLETYPE *)(audioFrame.buffer), audioFrame.samples);
        int nSamples = ptr.receiveSamples((SAMPLETYPE *)audioFrame.buffer, audioFrame.samples);
        audioFrame.samples = nSamples;
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
