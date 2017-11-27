package com.wen.hugo.userPage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wen.hugo.R;
import com.wen.hugo.activity.ImageBrowserActivity;
import com.wen.hugo.bean.Status;
import com.wen.hugo.util.ImageUtils;
import com.wen.hugo.widget.ListView.BaseListAdapter;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserPageListAdapter extends BaseListAdapter<Status> {

  private UserPageContract.Presenter mPresenter;

  public UserPageListAdapter(Context ctx) {
    super(ctx);
  }

  public void setPresenter(@NonNull UserPageContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public View getView(int position, View conView, ViewGroup parent) {
    if (conView == null) {
      conView = inflater.inflate(R.layout.status_item, null, false);
    }
    TextView nameView = findViewById(conView, R.id.nameView);
    TextView textView = findViewById(conView, R.id.statusText);
    ImageView avatarView = findViewById(conView, R.id.avatarView);
    ImageView imageView = findViewById(conView, R.id.statusImage);
    ImageView likeView = findViewById(conView, R.id.likeView);
    TextView likeCountView = findViewById(conView, R.id.likeCount);
    View likeLayout = findViewById(conView, R.id.likeLayout);
    TextView timeView = findViewById(conView, R.id.timeView);

    final Status status = datas.get(position);
    final AVStatus innerStatus = status.getInnerStatus();
    AVUser source = innerStatus.getSource();
    ImageUtils.displayAvatar(source, avatarView);
    nameView.setText(source.getUsername());

    if (TextUtils.isEmpty(innerStatus.getMessage())) {
      textView.setVisibility(View.GONE);
    } else {
      textView.setText(innerStatus.getMessage());
      textView.setVisibility(View.VISIBLE);
    }
    if (TextUtils.isEmpty(innerStatus.getImageUrl()) == false) {
      imageView.setVisibility(View.VISIBLE);
      ImageLoader.getInstance().displayImage(innerStatus.getImageUrl(),
          imageView, ImageUtils.normalImageOptions);
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(ctx, ImageBrowserActivity.class);
          intent.putExtra("url", innerStatus.getImageUrl());
          ctx.startActivity(intent);
        }
      });
    } else {
      imageView.setVisibility(View.GONE);
    }
    final AVObject detail = status.getDetail();

    final List<String> likes;
    if (detail.get("likes") != null) {
      likes = (List<String>) detail.get("likes");
    } else {
      likes = new ArrayList<String>();
    }

    int n = likes.size();
    if (n > 0) {
      likeCountView.setText(n + "");
    } else {
      likeCountView.setText("");
    }

    final AVUser user = AVUser.getCurrentUser();
    final String userId = user.getObjectId();
    final boolean contains = likes.contains(userId);
    if (contains) {
      likeView.setImageResource(R.drawable.status_ic_player_liked);
    } else {
      likeView.setImageResource(R.drawable.ic_player_like);
    }
    likeLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          mPresenter.updateStatusLikes(status,likes);
      }
    });

    timeView.setText(millisecs2DateString(innerStatus.getCreatedAt().getTime()));
    return conView;
  }

  public static PrettyTime prettyTime = new PrettyTime();

  public static String getDate(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
    return format.format(date);
  }

  public static String millisecs2DateString(long timestamp) {
    long gap = System.currentTimeMillis() - timestamp;
    if (gap < 1000 * 60 * 60 * 24) {
      String s = prettyTime.format(new Date(timestamp));
      return s.replace(" ", "");
    } else {
      return getDate(new Date(timestamp));
    }
  }

}
