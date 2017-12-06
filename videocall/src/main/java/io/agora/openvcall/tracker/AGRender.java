package io.agora.openvcall.tracker;

import android.util.Log;

import com.agora.tracker.bean.AGRenderResult;
import com.agora.tracker.bean.AGYuvFrame;

import java.nio.ByteBuffer;

import static com.agora.tracker.common.Config.TAG;

/**
 * Created by shijian on 2016/9/28.
 */
public class AGRender {
    //share mByteBuffer with native code
    private static ByteBuffer mByteBuffer;
    private static AGTrackerWrapper mTrackerWrapper;

    public static void init(AGTrackerWrapper wrapper) {
        mTrackerWrapper = wrapper;
    }

    public static void destory() {
        mTrackerWrapper = null;
    }


    /**
     * init buffer，call by native code
     *
     * @param len len
     * @return buffer
     */
    public static ByteBuffer createBuffer(int len) {
        mByteBuffer = ByteBuffer.allocateDirect(len);
        return mByteBuffer;
    }

    /**
     * render yuv frame,modify mByteBuffer,call by native code
     *
     * @param w        width
     * @param h        height
     * @param rotation rotation
     * @return -1,use the original yuv data
     */
    public static int renderYuvFrame(int w, int h, int rotation) {
        if (mTrackerWrapper == null || mByteBuffer == null) {
            Log.e("tracker", "mTrackerWrapper == null");
            return -1;
        }

        AGRenderResult mRenderResult = mTrackerWrapper.renderYuvFrame(new AGYuvFrame(mByteBuffer, w, h, rotation, 1));

        if (!mRenderResult.isRenderOK()) {
            Log.d(TAG, mRenderResult.toString());
        }

        return mRenderResult.isRenderOK() ? 0 : -1;
    }



}