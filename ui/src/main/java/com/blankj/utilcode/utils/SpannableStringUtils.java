//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan.Standard;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

public class SpannableStringUtils {
    private SpannableStringUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static SpannableStringUtils.Builder getBuilder(@NonNull CharSequence text) {
        return new SpannableStringUtils.Builder(text);
    }

    public static class Builder {
        private int defaultValue;
        private CharSequence text;
        private int flag;
        private int foregroundColor;
        private int backgroundColor;
        private int quoteColor;
        private boolean isLeadingMargin;
        private int first;
        private int rest;
        private boolean isBullet;
        private int gapWidth;
        private int bulletColor;
        private float proportion;
        private float xProportion;
        private boolean isStrikethrough;
        private boolean isUnderline;
        private boolean isSuperscript;
        private boolean isSubscript;
        private boolean isBold;
        private boolean isItalic;
        private boolean isBoldItalic;
        private String fontFamily;
        private Alignment align;
        private boolean imageIsBitmap;
        private Bitmap bitmap;
        private boolean imageIsDrawable;
        private Drawable drawable;
        private boolean imageIsUri;
        private Uri uri;
        private boolean imageIsResourceId;
        @DrawableRes
        private int resourceId;
        private ClickableSpan clickSpan;
        private String url;
        private boolean isBlur;
        private float radius;
        private Blur style;
        private SpannableStringBuilder mBuilder;

        private Builder(@NonNull CharSequence text) {
            this.defaultValue = 301989888;
            this.text = text;
            this.flag = 33;
            this.foregroundColor = this.defaultValue;
            this.backgroundColor = this.defaultValue;
            this.quoteColor = this.defaultValue;
            this.proportion = -1.0F;
            this.xProportion = -1.0F;
            this.mBuilder = new SpannableStringBuilder();
        }

        public SpannableStringUtils.Builder setFlag(int flag) {
            this.flag = flag;
            return this;
        }

        public SpannableStringUtils.Builder setForegroundColor(int color) {
            this.foregroundColor = color;
            return this;
        }

        public SpannableStringUtils.Builder setBackgroundColor(int color) {
            this.backgroundColor = color;
            return this;
        }

        public SpannableStringUtils.Builder setQuoteColor(int color) {
            this.quoteColor = color;
            return this;
        }

        public SpannableStringUtils.Builder setLeadingMargin(int first, int rest) {
            this.first = first;
            this.rest = rest;
            this.isLeadingMargin = true;
            return this;
        }

        public SpannableStringUtils.Builder setBullet(int gapWidth, int color) {
            this.gapWidth = gapWidth;
            this.bulletColor = color;
            this.isBullet = true;
            return this;
        }

        public SpannableStringUtils.Builder setProportion(float proportion) {
            this.proportion = proportion;
            return this;
        }

        public SpannableStringUtils.Builder setXProportion(float proportion) {
            this.xProportion = proportion;
            return this;
        }

        public SpannableStringUtils.Builder setStrikethrough() {
            this.isStrikethrough = true;
            return this;
        }

        public SpannableStringUtils.Builder setUnderline() {
            this.isUnderline = true;
            return this;
        }

        public SpannableStringUtils.Builder setSuperscript() {
            this.isSuperscript = true;
            return this;
        }

        public SpannableStringUtils.Builder setSubscript() {
            this.isSubscript = true;
            return this;
        }

        public SpannableStringUtils.Builder setBold() {
            this.isBold = true;
            return this;
        }

        public SpannableStringUtils.Builder setItalic() {
            this.isItalic = true;
            return this;
        }

        public SpannableStringUtils.Builder setBoldItalic() {
            this.isBoldItalic = true;
            return this;
        }

        public SpannableStringUtils.Builder setFontFamily(@Nullable String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }

        public SpannableStringUtils.Builder setAlign(@Nullable Alignment align) {
            this.align = align;
            return this;
        }

        public SpannableStringUtils.Builder setBitmap(@NonNull Bitmap bitmap) {
            this.bitmap = bitmap;
            this.imageIsBitmap = true;
            return this;
        }

        public SpannableStringUtils.Builder setDrawable(@NonNull Drawable drawable) {
            this.drawable = drawable;
            this.imageIsDrawable = true;
            return this;
        }

        public SpannableStringUtils.Builder setUri(@NonNull Uri uri) {
            this.uri = uri;
            this.imageIsUri = true;
            return this;
        }

        public SpannableStringUtils.Builder setResourceId(@DrawableRes int resourceId) {
            this.resourceId = resourceId;
            this.imageIsResourceId = true;
            return this;
        }

        public SpannableStringUtils.Builder setClickSpan(@NonNull ClickableSpan clickSpan) {
            this.clickSpan = clickSpan;
            return this;
        }

        public SpannableStringUtils.Builder setUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        public SpannableStringUtils.Builder setBlur(float radius, Blur style) {
            this.radius = radius;
            this.style = style;
            this.isBlur = true;
            return this;
        }

        public SpannableStringUtils.Builder append(@NonNull CharSequence text) {
            this.setSpan();
            this.text = text;
            return this;
        }

        public SpannableStringBuilder create() {
            this.setSpan();
            return this.mBuilder;
        }

        private void setSpan() {
            int start = this.mBuilder.length();
            this.mBuilder.append(this.text);
            int end = this.mBuilder.length();
            if(this.foregroundColor != this.defaultValue) {
                this.mBuilder.setSpan(new ForegroundColorSpan(this.foregroundColor), start, end, this.flag);
                this.foregroundColor = this.defaultValue;
            }

            if(this.backgroundColor != this.defaultValue) {
                this.mBuilder.setSpan(new BackgroundColorSpan(this.backgroundColor), start, end, this.flag);
                this.backgroundColor = this.defaultValue;
            }

            if(this.isLeadingMargin) {
                this.mBuilder.setSpan(new Standard(this.first, this.rest), start, end, this.flag);
                this.isLeadingMargin = false;
            }

            if(this.quoteColor != this.defaultValue) {
                this.mBuilder.setSpan(new QuoteSpan(this.quoteColor), start, end, 0);
                this.quoteColor = this.defaultValue;
            }

            if(this.isBullet) {
                this.mBuilder.setSpan(new BulletSpan(this.gapWidth, this.bulletColor), start, end, 0);
                this.isBullet = false;
            }

            if(this.proportion != -1.0F) {
                this.mBuilder.setSpan(new RelativeSizeSpan(this.proportion), start, end, this.flag);
                this.proportion = -1.0F;
            }

            if(this.xProportion != -1.0F) {
                this.mBuilder.setSpan(new ScaleXSpan(this.xProportion), start, end, this.flag);
                this.xProportion = -1.0F;
            }

            if(this.isStrikethrough) {
                this.mBuilder.setSpan(new StrikethroughSpan(), start, end, this.flag);
                this.isStrikethrough = false;
            }

            if(this.isUnderline) {
                this.mBuilder.setSpan(new UnderlineSpan(), start, end, this.flag);
                this.isUnderline = false;
            }

            if(this.isSuperscript) {
                this.mBuilder.setSpan(new SuperscriptSpan(), start, end, this.flag);
                this.isSuperscript = false;
            }

            if(this.isSubscript) {
                this.mBuilder.setSpan(new SubscriptSpan(), start, end, this.flag);
                this.isSubscript = false;
            }

            if(this.isBold) {
                this.mBuilder.setSpan(new StyleSpan(1), start, end, this.flag);
                this.isBold = false;
            }

            if(this.isItalic) {
                this.mBuilder.setSpan(new StyleSpan(2), start, end, this.flag);
                this.isItalic = false;
            }

            if(this.isBoldItalic) {
                this.mBuilder.setSpan(new StyleSpan(3), start, end, this.flag);
                this.isBoldItalic = false;
            }

            if(this.fontFamily != null) {
                this.mBuilder.setSpan(new TypefaceSpan(this.fontFamily), start, end, this.flag);
                this.fontFamily = null;
            }

            if(this.align != null) {
                this.mBuilder.setSpan(new android.text.style.AlignmentSpan.Standard(this.align), start, end, this.flag);
                this.align = null;
            }

            if(this.imageIsBitmap || this.imageIsDrawable || this.imageIsUri || this.imageIsResourceId) {
                if(this.imageIsBitmap) {
                    this.mBuilder.setSpan(new ImageSpan(Utils.getContext(), this.bitmap), start, end, this.flag);
                    this.bitmap = null;
                    this.imageIsBitmap = false;
                } else if(this.imageIsDrawable) {
                    this.mBuilder.setSpan(new ImageSpan(this.drawable), start, end, this.flag);
                    this.drawable = null;
                    this.imageIsDrawable = false;
                } else if(this.imageIsUri) {
                    this.mBuilder.setSpan(new ImageSpan(Utils.getContext(), this.uri), start, end, this.flag);
                    this.uri = null;
                    this.imageIsUri = false;
                } else {
                    this.mBuilder.setSpan(new ImageSpan(Utils.getContext(), this.resourceId), start, end, this.flag);
                    this.resourceId = 0;
                    this.imageIsResourceId = false;
                }
            }

            if(this.clickSpan != null) {
                this.mBuilder.setSpan(this.clickSpan, start, end, this.flag);
                this.clickSpan = null;
            }

            if(this.url != null) {
                this.mBuilder.setSpan(new URLSpan(this.url), start, end, this.flag);
                this.url = null;
            }

            if(this.isBlur) {
                this.mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(this.radius, this.style)), start, end, this.flag);
                this.isBlur = false;
            }

            this.flag = 33;
        }
    }
}
