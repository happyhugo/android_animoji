package com.wen.hugo;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.wen.hugo.util.EaseUIHelper;
import com.wen.hugo.util.ImageUtils;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(this, "wXd1JrqWS96GvggtDWRDgrwL-gzGzoHsz","WRDNre7WjjiwHvQNYm8Em7ep");
    AVOSCloud.setDebugLogEnabled(true);
    ImageUtils.initImageLoader(this);
    EaseUIHelper.init(getApplicationContext());
  }
}
