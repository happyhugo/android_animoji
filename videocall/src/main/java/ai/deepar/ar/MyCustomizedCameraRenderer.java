package ai.deepar.ar;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.ImageReader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by hugo on 2/7/18.
 */

public class MyCustomizedCameraRenderer extends SurfaceView implements AREventListener, SurfaceHolder.Callback {

    private final String TAG = MyCustomizedCameraRenderer.class.getSimpleName();
    private DeepAR deepAR;
    private Context mContext;
    private CameraGrabber cameraGrabber;
    private ImageReader mImageReader;

    public MyCustomizedCameraRenderer(Context context) {
        this(context,null);
    }

    public MyCustomizedCameraRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        this.getHolder().addCallback(this);
        deepAR = new DeepAR();
        deepAR.initialize(mContext, this,CameraResolutionPreset.P640x480);
        cameraGrabber = new CameraGrabber();
        cameraGrabber.initCamera(new CameraGrabberListener() {
            @Override
            public void onCameraInitialized() {
                cameraGrabber.setFrameReceiver(deepAR);
                cameraGrabber.startPreview();
            }

            @Override
            public void onCameraError(String errorMsg) {
                Log.e(TAG, errorMsg);
            }
        });
        mImageReader = ImageReader.newInstance(480, 640, PixelFormat.RGBA_8888, 10);
    }

    @Override
    public void error(String var1) {

    }

    @Override
    public void faceVisibilityChanged(boolean var1) {

    }

    @Override
    public void initialized() {
        gotoNext();
        deepAR.switchEffect("filters", "file:///android_asset/back_white");
        deepAR.startVideoRecording(mImageReader.getSurface());
        if(aiListener!=null){
            aiListener.onAIReady();
        }
    }

    String[] select = new String[]{"trump_anim","rabbit","cat","dog","chicken","monkey","pig","poo","alien","panda"};
    int index = -1;

    public void gotoNext() {
        index++;
        if(index==select.length){
            index = 0;
        }
        deepAR.switchEffect("masks", "file:///android_asset/"+select[index]);
    }

    public void gotoPrevious() {
        index--;
        if(index==-1){
            index = select.length-1;
        }
        deepAR.switchEffect("masks", "file:///android_asset/"+select[index]);
    }

    @Override
    public void recordStop() {
        deepAR.startVideoRecording(mImageReader.getSurface());
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.i(TAG,"surfaceChanged");
        deepAR.setRenderSurface(surfaceHolder.getSurface(), width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(TAG,"surfaceDestroyed");
        deepAR.setRenderSurface(null,0,0);
    }

    public void onDestroy() {
        Log.i(TAG,"onDestroy");
        cameraGrabber.setFrameReceiver(null);
        cameraGrabber.stopPreview();
        cameraGrabber.releaseCamera();
        cameraGrabber = null;
        deepAR.release();
    }


    private AiListener aiListener;

    public interface AiListener {
        void onAIReady();
    }

    public void addAilistener(AiListener aiListeners){
        this.aiListener = aiListeners;
    }

    public void addFrameAvailableListener(ImageReader.OnImageAvailableListener listener){
        mImageReader.setOnImageAvailableListener(listener,null);
    }
}
