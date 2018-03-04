/*
 * Decompiled with CFR 0_123.
 */
package ai.deepar.ar;

public enum CameraResolutionPreset {
    P640x480(0),
    P640x360(1),
    P1280x720(2);
    
    private int val;

    private CameraResolutionPreset(int n2) {
        this.val = n2;
    }

    public int getVal() {
        return this.val;
    }
}

