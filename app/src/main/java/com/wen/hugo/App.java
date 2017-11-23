package com.wen.hugo;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wen.hugo.ListView.StatusUtils;

import java.io.File;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(this, "wXd1JrqWS96GvggtDWRDgrwL-gzGzoHsz","WRDNre7WjjiwHvQNYm8Em7ep");
    AVOSCloud.setDebugLogEnabled(true);
    initImageLoader(this);
  }

  public static void initImageLoader(Context context) {
    File cacheDir = StorageUtils.getOwnCacheDirectory(context, "");
    ImageLoaderConfiguration config = StatusUtils.getImageLoaderConfig(context, cacheDir);
    ImageLoader.getInstance().init(config);
  }
}
