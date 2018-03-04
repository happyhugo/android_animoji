/*
 * Decompiled with CFR 0_123.
 */
package ai.deepar.ar;

public enum CameraOrientation {
    PORTRAIT(0),
    LANDSCAPE_LEFT(1),
    LANDSCAPE_RIGHT(2),
    PORTRAIT_INVERTED(3);
    
    private int val;

    private CameraOrientation(int n2) {
        this.val = n2;
    }

    public int getVal() {
        return this.val;
    }
}

