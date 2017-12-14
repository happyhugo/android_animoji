package com.wen.hugo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

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

    public synchronized static void displayAvatar(AVUser user, ImageView avatarView,ImageView avatarView2) {
        avatarView.setImageResource(getRandomColor(user.getUsername()));
        avatarView2.setImageResource(getRandomDrawable(user.getUsername()));
//        Glide.with(contexts).load(getRandomColor()).fitCenter().into(avatarView);

//        AVFile file = user.getAVFile("avatar");
//        if (file != null) {
//            String url = file.getUrl();
//            ImageLoader.getInstance().displayImage(url, avatarView, avatarImageOptions);
//        } else {
////      int avatarIds[] = new int[]{R.drawable.status_avatar0, R.drawable.status_avatar1,
////          R.drawable.status_avatar3, R.drawable.status_avatar4,
////          R.drawable.status_avatar5, R.drawable.status_avatar6};
////      String id = user.getObjectId();
////      if (userId2randomAvatar.get(id) == null) {
////        userId2randomAvatar.put(id, new Random().nextInt(avatarIds.length));
////      }
////      int randomN = userId2randomAvatar.get(id);
////      int avatarId = avatarIds[randomN];
////      avatarView.setImageResource(avatarId);
//        }
    }

    private static final Random RANDOM = new Random();

    public static int getRandomColor(String username) {

        switch ((username.length()+username.charAt(1))% 6) {
            default:
            case 0:
                return android.R.color.holo_green_light;
            case 1:
                return android.R.color.holo_blue_bright;
            case 2:
                return android.R.color.holo_orange_light;
            case 3:
                return android.R.color.holo_red_light;
            case 4:
                return android.R.color.holo_purple;
            case 5:
                return android.R.color.darker_gray;
        }
    }

    public static int getRandomDrawable(String username) {
        switch ((username.length()+username.charAt(1))% 13) {
            default:
            case 0:
                return R.drawable.avatar_fa_1;
            case 1:
                return R.drawable.avatar_fa_2;
            case 2:
                return R.drawable.avatar_fa_3;
            case 3:
                return R.drawable.avatar_fa_4;
            case 4:
                return R.drawable.avatar_fa_5;
            case 5:
                return R.drawable.avatar_fa_6;
            case 7:
                return R.drawable.avatar_ma_1;
            case 8:
                return R.drawable.avatar_ma_2;
            case 9:
                return R.drawable.avatar_ma_3;
            case 10:
                return R.drawable.avatar_ma_4;
            case 11:
                return R.drawable.avatar_ma_5;
            case 12:
                return R.drawable.avatar_ma_6;
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
