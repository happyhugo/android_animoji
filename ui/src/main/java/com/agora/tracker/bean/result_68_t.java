//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.bean;

import android.graphics.PointF;
import android.util.Log;

public class result_68_t {
    public cv_rect_t rect;
    public cv_pointf_t[] points_array;
    public float yaw;
    public float pitch;
    public float roll;
    public boolean mouth_open;
    public boolean brow_up;
    public boolean eye_status;

    public result_68_t() {
    }

    public result_68_t(cv_rect_t rect, cv_pointf_t[] points_array, float yaw, float pitch, float roll) {
        Log.e("Test", "new result_68_t");
        this.rect = rect;
        if(points_array.length != this.points_array.length) {
            throw new IllegalArgumentException("Wrong array size !");
        } else {
            this.points_array = points_array;
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
        }
    }

    public String toString() {
        return "Face Rect:" + this.rect;
    }

    public void scalePoints(float scaleWidth, float scaleHeight) {
        cv_pointf_t[] var3 = this.points_array;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            cv_pointf_t t = var3[var5];
            t.x *= scaleWidth;
            t.y *= scaleHeight;
        }

    }

    public PointF[] getPointsArray() {
        return this.points_array;
    }
}
