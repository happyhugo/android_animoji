/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 */
package ai.deepar.ar;

public interface AREventListener {
    public void error(String var1);

    public void faceVisibilityChanged(boolean var1);

    public void initialized();

    public void recordStop();
}

