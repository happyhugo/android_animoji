package ai.deepar.ar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.util.UUID;

public class DeepAR implements FrameReceiver {
    private static final String TAG = DeepAR.class.getSimpleName();
    private AssetManager assetManager;
    private CameraOrientation cameraOrientation = CameraOrientation.PORTRAIT;
    private static final int ENGINE_STATE_IDLE = 0;
    private static final int ENGINE_STATE_RUNNING = 2;
    private static final int ENGINE_STATE_SHUTDOWN_IN_PROGRESS = 3;
    private int engineState = 0;
    private Handler eventHandler;
    private AREventListener eventListener;
    public static final int RECORDING_STATE_IDLE = 0;
    public static final int RECORDING_STATE_IN_PROGRESS = 2;
    public static final int RECORDING_STATE_PREPARING = 1;
    public static final int RECORDING_STATE_STOPPING = 3;
    public int recordingState = 0;
    private CameraResolutionPreset resolutionPreset = CameraResolutionPreset.P640x480;

    static {
        System.loadLibrary("native-lib");
    }

    public void startVideoRecording(Surface surface) {
        if (this.recordingState != RECORDING_STATE_IDLE) {
            return;
        }
        Log.i(TAG,"startVideoRecording");
        this.setVideoSurface(surface, 0, 1, 0, 1);    //录像的时候调用这个，似乎就会回调过来
        this.recordingState = RECORDING_STATE_PREPARING;
    }


    public void stopVideoRecording() {         //设置参数，但是也没有调用setpaused
        if (this.recordingState != RECORDING_STATE_IN_PROGRESS) {
            return;
        }
        Log.i(TAG,"stopVideoRecording");
        this.recordingState = RECORDING_STATE_STOPPING;
        this.setVideoRecordingInProgress(false);
        this.stopVideoRecordingNative();

    }

    public void setRenderSurface(Surface surface, int n, int n2) {
        if (surface == null) {
            this.setPaused(true);
            if (this.recordingState == RECORDING_STATE_IN_PROGRESS) {
                this.stopVideoRecording();    //surface为空时，停止的动作，似乎应该先调用一下 setPaused
            }
            return;
        }
        if (this.engineState == ENGINE_STATE_SHUTDOWN_IN_PROGRESS) {
            Log.e(TAG, "Cannot setRenderSurface - shutdown in progress.");
            return;
        }
        if (this.isInitialized()) {
            this.setWindow(surface);
        } else {
            this.initializeAR(surface, assetManager, n, n2, this.resolutionPreset.getVal(), this.cameraOrientation.getVal());
            this.engineState = 1;
        }
        this.setPaused(false);
    }

    public void switchEffect(String string2, String string3) {
        Log.i(TAG,"switchEffect");
        //似乎可以支持多张脸
        String string4 = string3;
        if (string3 == null) {
            string4 = "none";
        }
        this.switchEffectNative(string2, string4);
    }

    public void release() {
        this.setPaused(true);
        this.engineState = ENGINE_STATE_SHUTDOWN_IN_PROGRESS;
        this.eventListener = null;
        this.shutdown();
    }

    public void initialize(Context context, AREventListener aREventListener, CameraResolutionPreset cameraResolutionPreset) {
        this.eventHandler = new Handler(Looper.getMainLooper());
        this.eventListener = aREventListener;
        this.assetManager = context.getResources().getAssets();
        this.resolutionPreset = cameraResolutionPreset;
        this.fetchAppInfo(context);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void messageFromNative(String string2, final String string3) {
        if (string2.equals("INITIALIZATION_FINISHED")) {
            this.initializationFinished();
            return;
        } else {
            if (string2.equals("RECORDING_STOPPED")) {
                this.videoRecordingStopped();
                return;
            }
            if (string2.equals("RECORDING_STARTED")) {
                this.videoRecordingStarted();
                return;
            }
            if (string2.equals("FACE_VISIBILITY_CHANGED")) {
                this.faceVisibilityChanged(string3);
                return;
            }
            if (string2.equals("FRAME_AVAILABLE_SOON")) {
                this.frameAvailableSoon();
                return;
            }
            if (string2.equals("SHUTDOWN_FINISHED")) {
                this.shutdownFinished();
                return;
            }
            if (string2.equals("FRAME_RENDERED")) {
                this.frameRendered();
                return;
            }
            if (string2.equals("ERROR")) {
                Log.i(TAG,"ERROR");
                this.eventHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        if (DeepAR.this.eventListener != null) {
                            DeepAR.this.eventListener.error(string3);
                        }
                    }
                });
                return;
            }
        }
    }

    private void initializationFinished() {
        //finish可能在很早的时候，初始化完后，才可以switch才有作用
        Log.i(TAG,"initializationFinished");
        this.engineState = ENGINE_STATE_RUNNING;
        this.eventHandler.post(new Runnable(){
            @Override
            public void run() {
                if (DeepAR.this.eventListener != null) {
                    DeepAR.this.eventListener.initialized();
                }
            }
        });
    }

    private void frameAvailableSoon() {
//        Log.i(TAG,"frameAvailableSoon");
    }

    private void frameRendered() {
//        Log.i(TAG,"frameRendered");
    }

    private void shutdownFinished() {
        Log.i(TAG,"shutdownFinished");
        this.engineState = ENGINE_STATE_IDLE;
    }

    private void faceVisibilityChanged(String string2) {
        Log.i(TAG,"faceVisibilityChanged");
        final boolean bl = string2.equals("true");
        this.eventHandler.post(new Runnable(){

            @Override
            public void run() {
                if (DeepAR.this.eventListener != null) {
                    DeepAR.this.eventListener.faceVisibilityChanged(bl);
                }
            }
        });
    }

    private void videoRecordingStarted() {
        Log.i(TAG,"videoRecordingStarted");
        this.recordingState = RECORDING_STATE_IN_PROGRESS;
        DeepAR.this.setVideoRecordingInProgress(true);
    }

    private void videoRecordingStopped() {
        Log.i(TAG,"videoRecordingStopped");
        this.recordingState = RECORDING_STATE_IDLE;
        this.eventHandler.post(new Runnable(){
            @Override
            public void run() {
                if (DeepAR.this.eventListener != null) {
                    DeepAR.this.eventListener.recordStop();
                }
            }
        });
    }

    @Override
    public void receiveFrame(ByteBuffer byteBuffer, int cameraOrientation) {
        this.frameUpdate(byteBuffer, cameraOrientation);
    }

    private void fetchAppInfo(Context object) {
        try {
            PackageInfo object2 = object.getPackageManager().getPackageInfo(object.getPackageName(), 0);
            String string2 = this.getUserId((Context)object);
            String string3 = object.getApplicationInfo().loadLabel(object.getPackageManager()).toString();
            String object22 = object2.versionName;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager)object.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            int n = displayMetrics.heightPixels;
            int n2 = displayMetrics.widthPixels;
            String objects = "" + n2 + "x" + n;
            n = Build.VERSION.SDK_INT;
            this.setAppInfo(string3, object22, objects, Build.MANUFACTURER, Build.MODEL, String.valueOf(n), string2);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private String getUserId(Context object) {
        SharedPreferences sharedPreferences = object.getSharedPreferences("DeepAR", 0);
        if (sharedPreferences.contains("userid")) {
            return sharedPreferences.getString("userid", "");
        }
        String objects = UUID.randomUUID().toString();
        SharedPreferences.Editor sharedPreferencess = sharedPreferences.edit();
        sharedPreferencess.putString("userid",objects);
        sharedPreferencess.apply();
        return objects;
    }

    private native void frameUpdate(ByteBuffer var1, int var2);
    private native boolean isInitialized();
    private native void initializeAR(Object var1, AssetManager var2, int var3, int var4, int var5, int var6);
    private native void setAppInfo(String var1, String var2, String var3, String var4, String var5, String var6, String var7);
    private native void setModelPath(String var1);
    private native void setPaused(boolean var1);
    private native void setWindow(Object var1);
    private native void shutdown();
    private native void setVideoSurface(Object var1, float var2, float var3, float var4, float var5);
    private native void switchEffectFaceNative(String var1, String var2, int var3);
    private native void switchEffectNative(String var1, String var2);
    private native void setVideoRecordingInProgress(boolean var1);
    private native void stopVideoRecordingNative();
}

