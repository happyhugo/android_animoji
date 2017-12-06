//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.bean;

public class cv_rect_t {
    public int x;
    public int y;
    public int width;
    public int height;

    public cv_rect_t() {
    }

    public cv_rect_t(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return String.format("x:%s,y:%s,w:%s,h:%s", new Object[]{Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.width), Integer.valueOf(this.height)});
    }
}
