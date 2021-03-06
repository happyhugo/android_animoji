package com.wen.hugo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wen.hugo.R;
import com.wen.hugo.util.ImageUtils;


public class ImageBrowserActivity extends Activity {
  String url;
  ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.image_brower_layout);
    imageView = (ImageView) findViewById(R.id.imageView);
    Intent intent = getIntent();
    url = intent.getStringExtra("url");
    ImageLoader.getInstance().displayImage(url, imageView, ImageUtils.normalImageOptions);
  }
}
