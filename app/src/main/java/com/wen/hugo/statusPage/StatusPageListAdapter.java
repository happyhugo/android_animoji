package com.wen.hugo.statusPage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.R;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.userPage.UserPageActivity;
import com.wen.hugo.util.ImageUtils;
import com.wen.hugo.widget.ListView.BaseListAdapter;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusPageListAdapter extends BaseListAdapter<Comment> {

  private StatusPageContract.Presenter mPresenter;

  public StatusPageListAdapter(Context ctx) {
    super(ctx);
  }

  public void setPresenter(@NonNull StatusPageContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override
  public View getView(int position, View conView, ViewGroup parent) {
    if (conView == null) {
      conView = inflater.inflate(R.layout.comment_item, null, false);
    }
    TextView nameView = findViewById(conView, R.id.tv_nick);
    TextView textView = findViewById(conView, R.id.tv_comment);
    ImageView avatarView = findViewById(conView, R.id.iv_avatar);
    TextView timeView = findViewById(conView, R.id.tv_time);

    final Comment comment = datas.get(position);
    final AVUser source = comment.getFrom();
    ImageUtils.displayAvatar(source, avatarView);
    nameView.setText(source.getUsername());
    if(comment.getComment().getCreatedAt()==null){
      timeView.setText(millisecs2DateString(System.currentTimeMillis()));
    }else{
      timeView.setText(millisecs2DateString(comment.getComment().getCreatedAt().getTime()));
    }
    if (comment.getReplayTo() == null) {
      textView.setText(comment.getComment().get(Comment.CONTENT).toString());
    } else {
      textView.setText(String.format("回复 %s 的评论：%s", comment.getReplayTo().getUsername(), comment.getComment().get(Comment.CONTENT).toString()));
    }

    avatarView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        UserPageActivity.go(ctx, source);
      }
    });

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
