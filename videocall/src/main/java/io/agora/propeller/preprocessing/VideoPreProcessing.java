package io.agora.propeller.preprocessing;

public class VideoPreProcessing {
    static {
        System.loadLibrary("apm-plugin-video-preprocessing");
    }

    public native void enablePreAudioProcessing(boolean enable);
}
