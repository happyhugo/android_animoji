package com.wen.hugo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
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

/**
 * Created by hugo on 11/26/17.
 */

public class ImageUtils {

    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "");
        ImageLoaderConfiguration config = ImageUtils.getImageLoaderConfig(context, cacheDir);
        ImageLoader.getInstance().init(config);
    }

    public synchronized static void displayAvatar(AVUser user, ImageView avatarView) {
        AVFile file = user.getAVFile("avatar");
        if (file != null) {
            String url = file.getUrl();
            ImageLoader.getInstance().displayImage(url, avatarView, avatarImageOptions);
        } else {
//      int avatarIds[] = new int[]{R.drawable.status_avatar0, R.drawable.status_avatar1,
//          R.drawable.status_avatar3, R.drawable.status_avatar4,
//          R.drawable.status_avatar5, R.drawable.status_avatar6};
//      String id = user.getObjectId();
//      if (userId2randomAvatar.get(id) == null) {
//        userId2randomAvatar.put(id, new Random().nextInt(avatarIds.length));
//      }
//      int randomN = userId2randomAvatar.get(id);
//      int avatarId = avatarIds[randomN];
//      avatarView.setImageResource(avatarId);
        }
    }

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
