//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ai.deepar.ar;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CameraGrabber {
    private static final String TAG = CameraGrabber.class.getSimpleName();
    private static final int NUMBER_OF_BUFFERS = 2;
    private static int currentCameraDevice = 1;
    private CameraGrabber.CameraHandlerThread mThread = null;

    public CameraGrabber() {
    }

    public CameraGrabber(int cameraDevice) {
        currentCameraDevice = cameraDevice;
    }

    public void setFrameReceiver(ai.deepar.ar.FrameReceiver receiver) {
        if(this.mThread != null) {
            this.mThread.setFrameReceiver(receiver);
        }

    }

    public void initCamera(ai.deepar.ar.CameraGrabberListener listener) {
        if(this.mThread == null) {
            this.mThread = new CameraGrabber.CameraHandlerThread(listener);
        }

        CameraGrabber.CameraHandlerThread var2 = this.mThread;
        synchronized(this.mThread) {
            this.mThread.openCamera();
        }
    }

    public void startPreview() {
        if(this.mThread != null && this.mThread.camera != null) {
            this.mThread.camera.startPreview();
        }

    }

    public void stopPreview() {
        if(this.mThread != null && this.mThread.camera != null) {
            this.mThread.camera.stopPreview();
        }

    }

    public void changeCameraDevice(int cameraDevice) {
        currentCameraDevice = cameraDevice;
        this.initCamera(new ai.deepar.ar.CameraGrabberListener() {
            public void onCameraInitialized() {
                CameraGrabber.this.startPreview();
            }

            public void onCameraError(String errorMsg) {
                Log.e(CameraGrabber.TAG, errorMsg);
            }
        });
    }

    public int getCurrCameraDevice() {
        return currentCameraDevice;
    }

    public void releaseCamera() {
        if(this.mThread != null) {
            this.mThread.releaseCamera();
        }

    }

    private static class CameraHandlerThread extends HandlerThread {
        Handler mHandler = null;
        public Camera camera;
        public SurfaceTexture surface;
        private ai.deepar.ar.FrameReceiver frameReceiver;
        private ByteBuffer[] buffers;
        private int currentBuffer = 0;
        private ai.deepar.ar.CameraGrabberListener listener;
        private int cameraOrientation;

        CameraHandlerThread(ai.deepar.ar.CameraGrabberListener listener) {
            super("CameraHandlerThread");
            this.listener = listener;
            this.start();
            this.mHandler = new Handler(this.getLooper());
        }

        synchronized void notifyCameraOpened() {
            this.notify();
        }

        synchronized void releaseCamera() {
            if(this.camera != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        CameraHandlerThread.this.camera.release();
                        CameraHandlerThread.this.camera = null;
                    }
                });
            }
        }

        synchronized void setFrameReceiver(ai.deepar.ar.FrameReceiver receiver) {
            this.frameReceiver = receiver;
            boolean isFrontCamera = CameraGrabber.currentCameraDevice == 1;
            this.mHandler.post(new Runnable() {
                public void run() {
                    if(CameraHandlerThread.this.camera != null) {
                        CameraHandlerThread.this.camera.setPreviewCallbackWithBuffer(new PreviewCallback() {
                            public void onPreviewFrame(byte[] data, Camera arg1) {
                                if(CameraHandlerThread.this.frameReceiver != null) {
                                    CameraHandlerThread.this.buffers[CameraHandlerThread.this.currentBuffer].put(data);
                                    CameraHandlerThread.this.buffers[CameraHandlerThread.this.currentBuffer].position(0);
                                    if(CameraHandlerThread.this.frameReceiver != null) {
                                        CameraHandlerThread.this.frameReceiver.receiveFrame(CameraHandlerThread.this.buffers[CameraHandlerThread.this.currentBuffer], CameraHandlerThread.this.cameraOrientation);
                                    }

                                    CameraHandlerThread.this.currentBuffer = (CameraHandlerThread.this.currentBuffer + 1) % 2;
                                }

                                if(CameraHandlerThread.this.camera != null) {
                                    CameraHandlerThread.this.camera.addCallbackBuffer(data);
                                }

                            }
                        });
                    }
                }
            });
        }

        public void init() {
            if(this.camera != null) {
                this.camera.stopPreview();
                this.camera.release();
                this.camera = null;
            }

            if(this.surface == null) {
                this.surface = new SurfaceTexture(0);
            }

            CameraInfo info = new CameraInfo();
            int cameraId = -1;
            int numberOfCameras = Camera.getNumberOfCameras();

            for(int i = 0; i < numberOfCameras; ++i) {
                Camera.getCameraInfo(i, info);
                if(info.facing == CameraGrabber.currentCameraDevice) {
                    this.cameraOrientation = info.orientation;
                    cameraId = i;
                    break;
                }
            }

            if(cameraId == -1) {
                if(this.listener != null) {
                    this.listener.onCameraError("Camera not found error.");
                }

            } else {
                try {
                    this.camera = Camera.open(cameraId);
                } catch (Exception var7) {
                    if(this.listener != null) {
                        this.listener.onCameraError("Could not open camera device. Could be used by another process.");
                    }

                    return;
                }

                Parameters params = this.camera.getParameters();
                params.setPreviewSize(640, 480);
                params.setPictureSize(640, 480);
                params.setPictureFormat(256);
                params.setJpegQuality(90);
                this.camera.setParameters(params);
                this.buffers = new ByteBuffer[2];

                for(int i = 0; i < 2; ++i) {
                    this.buffers[i] = ByteBuffer.allocateDirect(460800);
                    this.buffers[i].order(ByteOrder.nativeOrder());
                    this.buffers[i].position(0);
                    byte[] buffer = new byte[460800];
                    this.camera.addCallbackBuffer(buffer);
                }

                try {
                    this.camera.setPreviewTexture(this.surface);
                } catch (IOException var8) {
                    if(this.listener != null) {
                        this.listener.onCameraError("Error setting preview texture.");
                    }

                    return;
                }

                if(this.frameReceiver != null) {
                    this.setFrameReceiver(this.frameReceiver);
                }

                if(this.listener != null) {
                    this.listener.onCameraInitialized();
                }

            }
        }

        void openCamera() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    CameraHandlerThread.this.init();
                    CameraHandlerThread.this.notifyCameraOpened();
                }
            });

            try {
                this.wait();
            } catch (InterruptedException var2) {
                Log.w("E", "wait was interrupted");
            }

        }
    }
}
