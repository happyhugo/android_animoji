//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.bean;

public class AGTrackResult {
    public static final int IMAGE_ORIENTATION_UP = 0;
    public static final int IMAGE_ORIENTATION_LEFT = 1;
    public static final int IMAGE_ORIENTATION_DOWN = 2;
    public static final int IMAGE_ORIENTATION_RIGHT = 3;
    public static final int NO_TRACK_RESULT_VALUE = -1001;
    public static AGTrackResult NO_TRACK_RESULT = new AGTrackResult(-1001);
    int retCode;
    int faceTrackCount;
    result_68_t[] faceTrackArrs;
    private int imageOrientation;
    private boolean isFrontCamera;
    private boolean isScaled;

    public AGTrackResult(int retCode) {
        this(retCode, 0, new result_68_t[0]);
    }

    public AGTrackResult(int retCode, int faceTrackCount, result_68_t[] faceTrackArrs) {
        this.isScaled = false;
        this.retCode = retCode;
        this.faceTrackCount = faceTrackCount;
        this.faceTrackArrs = faceTrackArrs;
    }

    public int getRetCode() {
        return this.retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public int getFaceTrackCount() {
        return this.faceTrackCount;
    }

    public void setFaceTrackCount(int faceTrackCount) {
        this.faceTrackCount = faceTrackCount;
    }

    public result_68_t[] getFaceTrackArrs() {
        return this.faceTrackArrs;
    }

    public void setFaceTrackArrs(result_68_t[] faceTrackArrs) {
        this.faceTrackArrs = faceTrackArrs;
    }

    public boolean isTrackedFace() {
        return this.retCode == 0;
    }

    public String toString() {
        return String.format("RetCode:%s,face count:%s", new Object[]{Integer.valueOf(this.retCode), Integer.valueOf(this.faceTrackCount)});
    }

    public result_68_t getFaceTrackResult(int i) {
        return this.faceTrackArrs[i];
    }

    public void scaleTrackPoints(float scaleWidth, float scaleHeight) {
        if(!this.isScaled) {
            this.isScaled = true;

            for(int i = 0; i < this.faceTrackCount; ++i) {
                result_68_t t = this.faceTrackArrs[i];
                t.scalePoints(scaleWidth, scaleHeight);
            }

        }
    }

    public int getImageOrientation() {
        return this.imageOrientation;
    }

    public void setImageOrientation(int imageOrientation) {
        this.imageOrientation = imageOrientation;
    }

    public void setFrontCamera(boolean frontCamera) {
        this.isFrontCamera = frontCamera;
    }

    public boolean getIsFrontCamera() {
        return this.isFrontCamera;
    }
}
