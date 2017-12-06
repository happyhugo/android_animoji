//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.agora.tracker.bean.AGTrackResult;
import com.agora.tracker.bean.cv_pointf_t;
import com.agora.tracker.bean.cv_rect_t;
import com.agora.tracker.bean.result_68_t;
import com.agora.tracker.utils.FileCache;
import com.blankj.utilcode.utils.CloseUtils;
import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.kiwi.tracker.JNIFaceTracker;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

public class AGFaceTracker {
    private static final String TAG;
    public static final int CV_CLOCKWISE_ROTATE_0 = 0;
    public static final int CV_CLOCKWISE_ROTATE_90 = 1;
    public static final int CV_CLOCKWISE_ROTATE_180 = 2;
    public static final int CV_CLOCKWISE_ROTATE_270 = 3;
    public static final int FT_POINT_SIZE = 68;
    public static final int AG_FORMAT_RGBA = 0;
    public static final int AG_FORMAT_NV21 = 1;
    public static final int AG_E_DETECTOR_LOADFAILED = -3;
    public static final int AG_OK = 0;
    public static final int AG_E_INVALID_BUNDLEID = -2;
    public static final int AG_E_OUT_OF_DATE = -1;
    public static final int AG_E_INVALID_FACE_NUM = -5;
    public static final int AG_E_WRONG_IMAGE_FORMAT = -6;
    public static final int AG_E_ALIGNMENT_LOADFAILED = -4;
    public static final int AG_E_NO_DETECT_FACE = -8;
    public static final int MAX_TRACK_FACE = 5;
    static boolean DEBUG;
    private static final String models = "models";
    private float[] out_x;
    private float[] out_y;
    private int[] out_rect;
    private float[] out_angle;
    private boolean[] out_expression;
    private boolean initialized = false;
    private int initErrorCode;
    private AGTrackResult invalidKwTrackResult = new AGTrackResult(-8);
    private AGTrackResult noInitializedTrackResult = new AGTrackResult(-8);
    private static SPUtils spUtils;

    public static String getLicense() {
        return spUtils.getString("AGLicense");
    }

    public static void setLicense(String license) {
        if(StringUtils.isEmpty(license)) {
            Log.e(TAG, "license is null or empty,please check it");
        } else {
            spUtils.putString("AGLicense", license);
        }
    }

    public static String getModelPath(Context context) {
        String path = null;
        File dataDir = context.getApplicationContext().getExternalFilesDir("");
        if(dataDir != null) {
            path = dataDir.getAbsolutePath() + File.separator + "models";
        }

        return path;
    }

    public AGFaceTracker(String licenseStr) {
        byte count = 5;
        this.out_x = new float[count * 68];
        this.out_y = new float[count * 68];
        this.out_rect = new int[count * 4];
        this.out_angle = new float[count * 3];
        this.out_expression = new boolean[count * 3];
    }

    public boolean init(Context context) {
        String license = getLicense();
        if(StringUtils.isEmpty(license)) {
            String start = this.getLicenseFromFile(context);
            Log.i(TAG, "read license from local file,license value:" + start);
            setLicense(start);
            license = start;
        }

        if(StringUtils.isEmpty(license)) {
            Log.e(TAG, "agora license is null or empty");
            return false;
        } else {
            Log.e(TAG, "application id:" + context.getPackageName());
            long start1 = System.currentTimeMillis();
            String mModelPath = getModelPath(context);
            Log.i("Tracker", "dst mModelPath:" + mModelPath);
            synchronized(this.getClass()) {
                FileCache.copyFilesFassets(context, "models", mModelPath);
            }

            Log.i("Tracker", "copy modles cost:" + (System.currentTimeMillis() - start1));
            start1 = System.currentTimeMillis();
            int ret = JNIFaceTracker.init(context, mModelPath, license);
            Log.i("Tracker", "init cost:" + (System.currentTimeMillis() - start1) + ",ret:" + ret);
            if(ret != 0) {
                this.initErrorCode = ret;
                this.invalidKwTrackResult.setRetCode(ret);
                this.tipInitError(context, ret);
                String msg = "failed to init tracker,ret:" + ret + ",modelPath:" + mModelPath + ",exist:" + (new File(mModelPath)).exists();
                Log.e(TAG, msg);
                this.initialized = false;
                return false;
            } else {
                this.initialized = true;
                return true;
            }
        }
    }

    private void tipInitError(Context context, int ret) {
        String message = "failed to init tracker,error code:" + ret;
        String str = "";
        if(ret == -2) {
            str = ",invalid package name:" + context.getPackageName();
        } else if(ret == -1) {
            str = ",tracker is out of time";
        } else if(ret == -3) {
            str = ",failed to load model";
        }

        Toast.makeText(context, message + str, 1).show();
    }

    public AGTrackResult track(byte[] colorImage, int format, int maxfaceCount, int imageWidth, int imageHeight, int orientation, boolean isFrontCamera) {

            Log.d(TAG, String.format("track,byte:%s,w:%s,h:%s,o:%s", new Object[]{Integer.valueOf(colorImage.length), Integer.valueOf(imageWidth), Integer.valueOf(imageHeight), Integer.valueOf(orientation)}));


        if(!this.initialized) {
            Log.e(TAG, "tracker is not initialized");
            return this.noInitializedTrackResult;
        } else if(this.initErrorCode < 0) {
            Log.e(TAG, "failed to init tracker,ret code:" + this.initErrorCode);
            return this.invalidKwTrackResult;
        } else {
            long startTime = 0L;
            if(DEBUG) {
                startTime = System.currentTimeMillis();
            }

            if(colorImage == null) {
                return AGTrackResult.NO_TRACK_RESULT;
            } else {
                int rst = JNIFaceTracker.track(colorImage, format, maxfaceCount, orientation, imageWidth, imageHeight, this.out_x, this.out_y, this.out_rect, this.out_angle, this.out_expression);
                if(DEBUG) {
                    Log.d(TAG, "multi track time: " + (System.currentTimeMillis() - startTime) + "ms,ret:" + rst);
                }

                if(rst < 0) {
                    if(DEBUG) {
                        Log.d(TAG, "track result:" + rst);
                    }

                    return new AGTrackResult(rst);
                } else {
                    int faceCount = rst;
                    if(rst == 0) {
                        if(DEBUG) {
                            Log.e(TAG, "Error occur in face track,call to agora");
                        }

                        return new AGTrackResult(rst);
                    } else {
                        result_68_t[] array = new result_68_t[rst];

                        for(int ret = 0; ret < faceCount; ++ret) {
                            array[ret] = createResult68t(this.out_x, this.out_y, this.out_rect, this.out_angle, this.out_expression, ret);
                        }

                        AGTrackResult var14 = new AGTrackResult(0, faceCount, array);
                        var14.setImageOrientation(convert2ImageOrientation(orientation));
                        var14.setFrontCamera(isFrontCamera);
                        return var14;
                    }
                }
            }
        }
    }

    public static int convert2ImageOrientation(int orientation) {
        byte value = 3;
        switch(orientation) {
            case 0:
                value = 0;
                break;
            case 1:
                value = 1;
                break;
            case 2:
                value = 2;
                break;
            case 3:
                value = 3;
        }

        return value;
    }

    private static result_68_t createResult68t(float[] out_x, float[] out_y, int[] out_rect, float[] out_angle, boolean[] out_expression, int i) {
        if(DEBUG) {
            Log.i(TAG, "outAngle:" + Arrays.toString(out_angle) + ",outRecg:" + Arrays.toString(out_angle) + ",i:" + i);
        }

        result_68_t ret = new result_68_t();
        int j = i * 4;
        ret.rect = new cv_rect_t(out_rect[j + 0], out_rect[j + 1], out_rect[j + 2], out_rect[j + 3]);
        j = i * 3;
        ret.yaw = out_angle[j + 0];
        ret.pitch = out_angle[j + 1];
        ret.roll = out_angle[j + 2];
        j = i * 3;
        ret.mouth_open = out_expression[j + 0];
        ret.brow_up = out_expression[j + 1];
        ret.eye_status = out_expression[j + 2];
        j = i * 68;
        ret.points_array = toPoints_array(out_x, out_y, j);
        return ret;
    }

    private static cv_pointf_t[] toPoints_array(float[] out_x, float[] out_y, int j) {
        if(j + 68 > out_x.length && j + 68 > out_y.length) {
            throw new IllegalArgumentException("Wrong array size !");
        } else {
            cv_pointf_t[] pointFs = new cv_pointf_t[68];

            for(int i = 0; i < 68; ++i) {
                pointFs[i] = new cv_pointf_t(out_x[j + i], out_y[j + i]);
            }

            return pointFs;
        }
    }

    public void destory() {
        if(this.initErrorCode < 0) {
            Log.e(TAG, "failed to init tracker,ret code:" + this.initErrorCode);
        } else {
            long start_destroy = System.currentTimeMillis();
            JNIFaceTracker.destory();
            long end_destroy = System.currentTimeMillis();
            if(DEBUG) {
                Log.i(TAG, "destroy cost " + (end_destroy - start_destroy) + " ms");
            }

        }
    }

    private String getLicenseFromFile(Context context) {
        String text = null;
        InputStream is = null;

        try {
            is = this.getInputStream(context, "KiwiFace.lic");
            if(is == null) {
                is = this.getInputStream(context, "AgLenses.lic");
            }

            if(is == null) {
                String e1 = "can not find license file and crash";
                Log.e("tracker", e1);
                throw new Exception(e1);
            }

            int e = is.available();
            byte[] buffer = new byte[e];
            is.read(buffer);
            text = new String(buffer, "utf-8");
        } catch (Exception var9) {
            Log.e("tracker", var9.toString());
        } finally {
            CloseUtils.closeIO(new Closeable[]{is});
        }

        return text;
    }

    private InputStream getInputStream(Context context, String fileName) {
        InputStream is = null;

        try {
            is = context.getAssets().open(fileName);
            Log.i("tracker", "read license from " + fileName);
        } catch (IOException var5) {
            ;
        }

        return is;
    }

    static {
        Log.e("tracker", "agora filter build time:" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)).format(Long.valueOf(1503990070690L)));
        TAG = AGFaceTracker.class.getName();
        DEBUG = true;
        spUtils = new SPUtils("AGFace");
    }
}
