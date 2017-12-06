//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.utils;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.view.WindowManager;

public class FTCameraUtils {
    public FTCameraUtils() {
    }

    public static int getOrientation(int currentFacingId) {
        CameraInfo camInfo = new CameraInfo();
        Camera.getCameraInfo(currentFacingId, camInfo);
        return camInfo.orientation;
    }

    public static int getOrientation(Context context, int currentFacingId) {
        int degree = getDeviceRotationDegree(context);
        CameraInfo camInfo = new CameraInfo();
        Camera.getCameraInfo(currentFacingId, camInfo);
        int orientation;
        if(currentFacingId == 1) {
            orientation = (camInfo.orientation + degree) % 360;
        } else {
            orientation = (camInfo.orientation - degree + 360) % 360;
        }

        return orientation;
    }

    private static int getDisplayDefaultRotation(Context ctx) {
        WindowManager windowManager = (WindowManager)ctx.getSystemService("window");
        return windowManager.getDefaultDisplay().getRotation();
    }

    private static int getDeviceRotationDegree(Context ctx) {
        switch(getDisplayDefaultRotation(ctx)) {
            case 0:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
            default:
                return 0;
        }
    }
}
