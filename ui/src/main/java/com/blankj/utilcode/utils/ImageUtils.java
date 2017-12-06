//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.RenderScript.RSMessageHandler;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    private ImageUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap, CompressFormat format) {
        if(bitmap == null) {
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(format, 100, baos);
            return baos.toByteArray();
        }
    }

    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return bytes != null && bytes.length != 0?BitmapFactory.decodeByteArray(bytes, 0, bytes.length):null;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return drawable == null?null:((BitmapDrawable)drawable).getBitmap();
    }

    public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
        return bitmap == null?null:new BitmapDrawable(res, bitmap);
    }

    public static byte[] drawable2Bytes(Drawable drawable, CompressFormat format) {
        return drawable == null?null:bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
        return res == null?null:bitmap2Drawable(res, bytes2Bitmap(bytes));
    }

    public static Bitmap view2Bitmap(View view) {
        if(view == null) {
            return null;
        } else {
            Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(ret);
            Drawable bgDrawable = view.getBackground();
            if(bgDrawable != null) {
                bgDrawable.draw(canvas);
            } else {
                canvas.drawColor(-1);
            }

            view.draw(canvas);
            return ret;
        }
    }

    private static int calculateInSampleSize(Options options, int maxWidth, int maxHeight) {
        if(maxWidth != 0 && maxHeight != 0) {
            int height = options.outHeight;
            int width = options.outWidth;

            int inSampleSize;
            for(inSampleSize = 1; (height >>= 1) >= maxHeight && (width >>= 1) >= maxWidth; inSampleSize <<= 1) {
                ;
            }

            return inSampleSize;
        } else {
            return 1;
        }
    }

    public static Bitmap getBitmap(File file) {
        if(file == null) {
            return null;
        } else {
            BufferedInputStream is = null;

            Object var3;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
                Bitmap e = BitmapFactory.decodeStream(is);
                return e;
            } catch (FileNotFoundException var7) {
                var7.printStackTrace();
                var3 = null;
            } finally {
                CloseUtils.closeIO(new Closeable[]{is});
            }

            return (Bitmap)var3;
        }
    }

    public static Bitmap getBitmap(File file, int maxWidth, int maxHeight) {
        if(file == null) {
            return null;
        } else {
            BufferedInputStream is = null;

            Bitmap var5;
            try {
                Options e = new Options();
                e.inJustDecodeBounds = true;
                is = new BufferedInputStream(new FileInputStream(file));
                BitmapFactory.decodeStream(is, (Rect)null, e);
                e.inSampleSize = calculateInSampleSize(e, maxWidth, maxHeight);
                e.inJustDecodeBounds = false;
                var5 = BitmapFactory.decodeStream(is, (Rect)null, e);
                return var5;
            } catch (FileNotFoundException var9) {
                var9.printStackTrace();
                var5 = null;
            } finally {
                CloseUtils.closeIO(new Closeable[]{is});
            }

            return var5;
        }
    }

    public static Bitmap getBitmap(String filePath) {
        return StringUtils.isSpace(filePath)?null:BitmapFactory.decodeFile(filePath);
    }

    public static Bitmap getBitmap(String filePath, int maxWidth, int maxHeight) {
        if(StringUtils.isSpace(filePath)) {
            return null;
        } else {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(filePath, options);
        }
    }

    public static Bitmap getBitmap(InputStream is) {
        return is == null?null:BitmapFactory.decodeStream(is);
    }

    public static Bitmap getBitmap(InputStream is, int maxWidth, int maxHeight) {
        if(is == null) {
            return null;
        } else {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, (Rect)null, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(is, (Rect)null, options);
        }
    }

    public static Bitmap getBitmap(byte[] data, int offset) {
        return data.length == 0?null:BitmapFactory.decodeByteArray(data, offset, data.length);
    }

    public static Bitmap getBitmap(byte[] data, int offset, int maxWidth, int maxHeight) {
        if(data.length == 0) {
            return null;
        } else {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, offset, data.length, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(data, offset, data.length, options);
        }
    }

    public static Bitmap getBitmap(Resources res, int id) {
        return res == null?null:BitmapFactory.decodeResource(res, id);
    }

    public static Bitmap getBitmap(Resources res, int id, int maxWidth, int maxHeight) {
        if(res == null) {
            return null;
        } else {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, id, options);
        }
    }

    public static Bitmap getBitmap(FileDescriptor fd) {
        return fd == null?null:BitmapFactory.decodeFileDescriptor(fd);
    }

    public static Bitmap getBitmap(FileDescriptor fd, int maxWidth, int maxHeight) {
        if(fd == null) {
            return null;
        } else {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, (Rect)null, options);
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFileDescriptor(fd, (Rect)null, options);
        }
    }

    public static Bitmap scale(Bitmap src, int newWidth, int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    public static Bitmap scale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap scale(Bitmap src, float scaleWidth, float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    public static Bitmap scale(Bitmap src, float scaleWidth, float scaleHeight, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            Matrix matrix = new Matrix();
            matrix.setScale(scaleWidth, scaleHeight);
            Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap clip(Bitmap src, int x, int y, int width, int height) {
        return clip(src, x, y, width, height, false);
    }

    public static Bitmap clip(Bitmap src, int x, int y, int width, int height, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            Bitmap ret = Bitmap.createBitmap(src, x, y, width, height);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap skew(Bitmap src, float kx, float ky) {
        return skew(src, kx, ky, 0.0F, 0.0F, false);
    }

    public static Bitmap skew(Bitmap src, float kx, float ky, boolean recycle) {
        return skew(src, kx, ky, 0.0F, 0.0F, recycle);
    }

    public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py) {
        return skew(src, kx, ky, px, py, false);
    }

    public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            Matrix matrix = new Matrix();
            matrix.setSkew(kx, ky, px, py);
            Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap rotate(Bitmap src, int degrees, float px, float py) {
        return rotate(src, degrees, px, py, false);
    }

    public static Bitmap rotate(Bitmap src, int degrees, float px, float py, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else if(degrees == 0) {
            return src;
        } else {
            Matrix matrix = new Matrix();
            matrix.setRotate((float)degrees, px, py);
            Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static int getRotateDegree(String filePath) {
        short degree = 0;

        try {
            ExifInterface e = new ExifInterface(filePath);
            int orientation = e.getAttributeInt("Orientation", 1);
            switch(orientation) {
                case 3:
                    degree = 180;
                    break;
                case 6:
                default:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return degree;
    }

    public static Bitmap toRound(Bitmap src) {
        return toRound(src, false);
    }

    public static Bitmap toRound(Bitmap src, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            int width = src.getWidth();
            int height = src.getHeight();
            int radius = Math.min(width, height) >> 1;
            Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
            Paint paint = new Paint();
            Canvas canvas = new Canvas(ret);
            Rect rect = new Rect(0, 0, width, height);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawCircle((float)(width >> 1), (float)(height >> 1), (float)radius, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(src, rect, rect, paint);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap toRoundCorner(Bitmap src, float radius) {
        return toRoundCorner(src, radius, false);
    }

    public static Bitmap toRoundCorner(Bitmap src, float radius, boolean recycle) {
        if(null == src) {
            return null;
        } else {
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
            Paint paint = new Paint();
            Canvas canvas = new Canvas(ret);
            Rect rect = new Rect(0, 0, width, height);
            paint.setAntiAlias(true);
            canvas.drawRoundRect(new RectF(rect), radius, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(src, rect, rect, paint);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap fastBlur(Bitmap src, float scale, float radius) {
        return fastBlur(src, scale, radius, false);
    }

    public static Bitmap fastBlur(Bitmap src, float scale, float radius, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            int width = src.getWidth();
            int height = src.getHeight();
            int scaleWidth = (int)((float)width * scale + 0.5F);
            int scaleHeight = (int)((float)height * scale + 0.5F);
            if(scaleWidth != 0 && scaleHeight != 0) {
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(src, scaleWidth, scaleHeight, true);
                Paint paint = new Paint(3);
                Canvas canvas = new Canvas();
                PorterDuffColorFilter filter = new PorterDuffColorFilter(0, Mode.SRC_ATOP);
                paint.setColorFilter(filter);
                canvas.scale(scale, scale);
                canvas.drawBitmap(scaleBitmap, 0.0F, 0.0F, paint);
                if(VERSION.SDK_INT >= 17) {
                    scaleBitmap = renderScriptBlur(Utils.getContext(), scaleBitmap, radius);
                } else {
                    scaleBitmap = stackBlur(scaleBitmap, (int)radius, recycle);
                }

                if(scale == 1.0F) {
                    return scaleBitmap;
                } else {
                    Bitmap ret = Bitmap.createScaledBitmap(scaleBitmap, width, height, true);
                    if(scaleBitmap != null && !scaleBitmap.isRecycled()) {
                        scaleBitmap.recycle();
                    }

                    if(recycle && !src.isRecycled()) {
                        src.recycle();
                    }

                    return ret;
                }
            } else {
                return null;
            }
        }
    }

    @TargetApi(17)
    public static Bitmap renderScriptBlur(Context context, Bitmap src, float radius) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            RenderScript rs = null;

            try {
                rs = RenderScript.create(context);
                rs.setMessageHandler(new RSMessageHandler());
                Allocation input = Allocation.createFromBitmap(rs, src, MipmapControl.MIPMAP_NONE, 1);
                Allocation output = Allocation.createTyped(rs, input.getType());
                ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                if(radius > 25.0F) {
                    radius = 25.0F;
                } else if(radius <= 0.0F) {
                    radius = 1.0F;
                }

                blurScript.setInput(input);
                blurScript.setRadius(radius);
                blurScript.forEach(output);
                output.copyTo(src);
            } finally {
                if(rs != null) {
                    rs.destroy();
                }

            }

            return src;
        }
    }

    public static Bitmap stackBlur(Bitmap src, int radius, boolean recycle) {
        Bitmap ret;
        if(recycle) {
            ret = src;
        } else {
            ret = src.copy(src.getConfig(), true);
        }

        if(radius < 1) {
            return null;
        } else {
            int w = ret.getWidth();
            int h = ret.getHeight();
            int[] pix = new int[w * h];
            ret.getPixels(pix, 0, w, 0, 0, w, h);
            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;
            int[] r = new int[wh];
            int[] g = new int[wh];
            int[] b = new int[wh];
            int[] vmin = new int[Math.max(w, h)];
            int divsum = div + 1 >> 1;
            divsum *= divsum;
            int[] dv = new int[256 * divsum];

            int i;
            for(i = 0; i < 256 * divsum; ++i) {
                dv[i] = i / divsum;
            }

            int yi = 0;
            int yw = 0;
            int[][] stack = new int[div][3];
            int r1 = radius + 1;

            int rsum;
            int gsum;
            int bsum;
            int x;
            int y;
            int p;
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int routsum;
            int goutsum;
            int boutsum;
            int rinsum;
            int ginsum;
            int binsum;
            for(y = 0; y < h; ++y) {
                bsum = 0;
                gsum = 0;
                rsum = 0;
                boutsum = 0;
                goutsum = 0;
                routsum = 0;
                binsum = 0;
                ginsum = 0;
                rinsum = 0;

                for(i = -radius; i <= radius; ++i) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 16711680) >> 16;
                    sir[1] = (p & '\uff00') >> 8;
                    sir[2] = p & 255;
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if(i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }

                stackpointer = radius;

                for(x = 0; x < w; ++x) {
                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];
                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;
                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];
                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];
                    if(y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }

                    p = pix[yw + vmin[x]];
                    sir[0] = (p & 16711680) >> 16;
                    sir[1] = (p & '\uff00') >> 8;
                    sir[2] = p & 255;
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;
                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer % div];
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];
                    ++yi;
                }

                yw += w;
            }

            for(x = 0; x < w; ++x) {
                bsum = 0;
                gsum = 0;
                rsum = 0;
                boutsum = 0;
                goutsum = 0;
                routsum = 0;
                binsum = 0;
                ginsum = 0;
                rinsum = 0;
                int yp = -radius * w;

                for(i = -radius; i <= radius; ++i) {
                    yi = Math.max(0, yp) + x;
                    sir = stack[i + radius];
                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];
                    rbs = r1 - Math.abs(i);
                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;
                    if(i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if(i < hm) {
                        yp += w;
                    }
                }

                yi = x;
                stackpointer = radius;

                for(y = 0; y < h; ++y) {
                    pix[yi] = -16777216 & pix[yi] | dv[rsum] << 16 | dv[gsum] << 8 | dv[bsum];
                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;
                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];
                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];
                    if(x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }

                    p = x + vmin[y];
                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;
                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];
                    yi += w;
                }
            }

            ret.setPixels(pix, 0, w, 0, 0, w, h);
            return ret;
        }
    }

    public static Bitmap addFrame(Bitmap src, int borderWidth, int color) {
        return addFrame(src, borderWidth, color, false);
    }

    public static Bitmap addFrame(Bitmap src, int borderWidth, int color, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            int doubleBorder = borderWidth << 1;
            int newWidth = src.getWidth() + doubleBorder;
            int newHeight = src.getHeight() + doubleBorder;
            Bitmap ret = Bitmap.createBitmap(newWidth, newHeight, src.getConfig());
            Canvas canvas = new Canvas(ret);
            Rect rect = new Rect(0, 0, newWidth, newHeight);
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth((float)doubleBorder);
            canvas.drawRect(rect, paint);
            canvas.drawBitmap(src, (float)borderWidth, (float)borderWidth, (Paint)null);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap addReflection(Bitmap src, int reflectionHeight) {
        return addReflection(src, reflectionHeight, false);
    }

    public static Bitmap addReflection(Bitmap src, int reflectionHeight, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            boolean REFLECTION_GAP = false;
            int srcWidth = src.getWidth();
            int srcHeight = src.getHeight();
            Matrix matrix = new Matrix();
            matrix.preScale(1.0F, -1.0F);
            Bitmap reflectionBitmap = Bitmap.createBitmap(src, 0, srcHeight - reflectionHeight, srcWidth, reflectionHeight, matrix, false);
            Bitmap ret = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.getConfig());
            Canvas canvas = new Canvas(ret);
            canvas.drawBitmap(src, 0.0F, 0.0F, (Paint)null);
            canvas.drawBitmap(reflectionBitmap, 0.0F, (float)(srcHeight + 0), (Paint)null);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0.0F, (float)srcHeight, 0.0F, (float)(ret.getHeight() + 0), 1895825407, 16777215, TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            canvas.drawRect(0.0F, (float)(srcHeight + 0), (float)srcWidth, (float)ret.getHeight(), paint);
            if(!reflectionBitmap.isRecycled()) {
                reflectionBitmap.recycle();
            }

            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap addTextWatermark(Bitmap src, String content, int textSize, int color, float x, float y) {
        return addTextWatermark(src, content, (float)textSize, color, x, y, false);
    }

    public static Bitmap addTextWatermark(Bitmap src, String content, float textSize, int color, float x, float y, boolean recycle) {
        if(!isEmptyBitmap(src) && content != null) {
            Bitmap ret = src.copy(src.getConfig(), true);
            Paint paint = new Paint(1);
            Canvas canvas = new Canvas(ret);
            paint.setColor(color);
            paint.setTextSize(textSize);
            Rect bounds = new Rect();
            paint.getTextBounds(content, 0, content.length(), bounds);
            canvas.drawText(content, x, y + textSize, paint);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        } else {
            return null;
        }
    }

    public static Bitmap addImageWatermark(Bitmap src, Bitmap watermark, int x, int y, int alpha) {
        return addImageWatermark(src, watermark, x, y, alpha, false);
    }

    public static Bitmap addImageWatermark(Bitmap src, Bitmap watermark, int x, int y, int alpha, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            Bitmap ret = src.copy(src.getConfig(), true);
            if(!isEmptyBitmap(watermark)) {
                Paint paint = new Paint(1);
                Canvas canvas = new Canvas(ret);
                paint.setAlpha(alpha);
                canvas.drawBitmap(watermark, (float)x, (float)y, paint);
            }

            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap toAlpha(Bitmap src) {
        return toAlpha(src, Boolean.valueOf(false));
    }

    public static Bitmap toAlpha(Bitmap src, Boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            Bitmap ret = src.extractAlpha();
            if(recycle.booleanValue() && !src.isRecycled()) {
                src.recycle();
            }

            return ret;
        }
    }

    public static Bitmap toGray(Bitmap src) {
        return toGray(src, false);
    }

    public static Bitmap toGray(Bitmap src, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            Bitmap grayBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(grayBitmap);
            Paint paint = new Paint();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0F);
            ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
            paint.setColorFilter(colorMatrixColorFilter);
            canvas.drawBitmap(src, 0.0F, 0.0F, paint);
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return grayBitmap;
        }
    }

    public static boolean save(Bitmap src, String filePath, CompressFormat format) {
        return save(src, FileUtils.getFileByPath(filePath), format, false);
    }

    public static boolean save(Bitmap src, File file, CompressFormat format) {
        return save(src, file, format, false);
    }

    public static boolean save(Bitmap src, String filePath, CompressFormat format, boolean recycle) {
        return save(src, FileUtils.getFileByPath(filePath), format, recycle);
    }

    public static boolean save(Bitmap src, File file, CompressFormat format, boolean recycle) {
        if(!isEmptyBitmap(src) && FileUtils.createOrExistsFile(file)) {
            System.out.println(src.getWidth() + ", " + src.getHeight());
            BufferedOutputStream os = null;
            boolean ret = false;

            try {
                os = new BufferedOutputStream(new FileOutputStream(file));
                ret = src.compress(format, 100, os);
                if(recycle && !src.isRecycled()) {
                    src.recycle();
                }
            } catch (IOException var10) {
                var10.printStackTrace();
            } finally {
                CloseUtils.closeIO(new Closeable[]{os});
            }

            return ret;
        } else {
            return false;
        }
    }

    public static boolean isImage(File file) {
        return file != null && isImage(file.getPath());
    }

    public static boolean isImage(String filePath) {
        String path = filePath.toUpperCase();
        return path.endsWith(".PNG") || path.endsWith(".JPG") || path.endsWith(".JPEG") || path.endsWith(".BMP") || path.endsWith(".GIF");
    }

    public static String getImageType(String filePath) {
        return getImageType(FileUtils.getFileByPath(filePath));
    }

    public static String getImageType(File file) {
        if(file == null) {
            return null;
        } else {
            FileInputStream is = null;

            Object var3;
            try {
                is = new FileInputStream(file);
                String e = getImageType((InputStream)is);
                return e;
            } catch (IOException var7) {
                var7.printStackTrace();
                var3 = null;
            } finally {
                CloseUtils.closeIO(new Closeable[]{is});
            }

            return (String)var3;
        }
    }

    public static String getImageType(InputStream is) {
        if(is == null) {
            return null;
        } else {
            try {
                byte[] e = new byte[8];
                return is.read(e, 0, 8) != -1?getImageType(e):null;
            } catch (IOException var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    public static String getImageType(byte[] bytes) {
        return isJPEG(bytes)?"JPEG":(isGIF(bytes)?"GIF":(isPNG(bytes)?"PNG":(isBMP(bytes)?"BMP":null)));
    }

    private static boolean isJPEG(byte[] b) {
        return b.length >= 2 && b[0] == -1 && b[1] == -40;
    }

    private static boolean isGIF(byte[] b) {
        return b.length >= 6 && b[0] == 71 && b[1] == 73 && b[2] == 70 && b[3] == 56 && (b[4] == 55 || b[4] == 57) && b[5] == 97;
    }

    private static boolean isPNG(byte[] b) {
        return b.length >= 8 && b[0] == -119 && b[1] == 80 && b[2] == 78 && b[3] == 71 && b[4] == 13 && b[5] == 10 && b[6] == 26 && b[7] == 10;
    }

    private static boolean isBMP(byte[] b) {
        return b.length >= 2 && b[0] == 66 && b[1] == 77;
    }

    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public static Bitmap compressByScale(Bitmap src, int newWidth, int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    public static Bitmap compressByScale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
        return scale(src, newWidth, newHeight, recycle);
    }

    public static Bitmap compressByScale(Bitmap src, float scaleWidth, float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    public static Bitmap compressByScale(Bitmap src, float scaleWidth, float scaleHeight, boolean recycle) {
        return scale(src, scaleWidth, scaleHeight, recycle);
    }

    public static Bitmap compressByQuality(Bitmap src, int quality) {
        return compressByQuality(src, quality, false);
    }

    public static Bitmap compressByQuality(Bitmap src, int quality, boolean recycle) {
        if(!isEmptyBitmap(src) && quality >= 0 && quality <= 100) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(CompressFormat.JPEG, quality, baos);
            byte[] bytes = baos.toByteArray();
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    public static Bitmap compressByQuality(Bitmap src, long maxByteSize) {
        return compressByQuality(src, maxByteSize, false);
    }

    public static Bitmap compressByQuality(Bitmap src, long maxByteSize, boolean recycle) {
        if(!isEmptyBitmap(src) && maxByteSize > 0L) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            src.compress(CompressFormat.JPEG, quality, baos);

            while((long)baos.toByteArray().length > maxByteSize && quality > 0) {
                baos.reset();
                quality -= 5;
                src.compress(CompressFormat.JPEG, quality, baos);
            }

            if(quality < 0) {
                return null;
            } else {
                byte[] bytes = baos.toByteArray();
                if(recycle && !src.isRecycled()) {
                    src.recycle();
                }

                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        } else {
            return null;
        }
    }

    public static Bitmap compressBySampleSize(Bitmap src, int sampleSize) {
        return compressBySampleSize(src, sampleSize, false);
    }

    public static Bitmap compressBySampleSize(Bitmap src, int sampleSize, boolean recycle) {
        if(isEmptyBitmap(src)) {
            return null;
        } else {
            Options options = new Options();
            options.inSampleSize = sampleSize;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            if(recycle && !src.isRecycled()) {
                src.recycle();
            }

            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }
    }
}
