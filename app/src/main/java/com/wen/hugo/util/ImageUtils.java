package com.wen.hugo.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wen.hugo.R;

import java.io.File;
import java.util.Random;

/**
 * Created by hugo on 11/26/17.
 */

public class ImageUtils {

    private static Context contexts;

    public static void initImageLoader(Context context) {
        contexts = context;

        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "");
        ImageLoaderConfiguration config = ImageUtils.getImageLoaderConfig(context, cacheDir);
        ImageLoader.getInstance().init(config);
    }

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.cheese_1;
            case 1:
                return R.drawable.cheese_2;
            case 2:
                return R.drawable.cheese_3;
            case 3:
                return R.drawable.cheese_4;
            case 4:
                return R.drawable.cheese_5;
        }
    }


    private static final Random RANDOM = new Random();


    public static DisplayImageOptions avatarImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_user_avatar)
            .showImageForEmptyUri(R.drawable.default_user_avatar)
            .showImageOnFail(R.drawable.default_user_avatar)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
            //.displayer(new RoundedBitmapDisplayer(20))
            //.displayer(new FadeInBitmapDisplayer(100))// 淡入
            .build();

    public static ImageLoaderConfiguration getImageLoaderConfig(Context context, File cacheDir) {
        return new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                //.writeDebugLogs() // Remove for release app
                .build();
    }


    public static DisplayImageOptions normalImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.empty_photo)
            .showImageForEmptyUri(R.drawable.empty_photo)
            .showImageOnFail(R.drawable.image_load_fail)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
            //.displayer(new RoundedBitmapDisplayer(20))
            //.displayer(new FadeInBitmapDisplayer(100))// 淡入
            .build();

}
