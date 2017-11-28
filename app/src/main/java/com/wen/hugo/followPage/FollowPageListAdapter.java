package com.wen.hugo.followPage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.R;
import com.wen.hugo.util.ImageUtils;
import com.wen.hugo.widget.ListView.BaseListAdapter;


public class FollowPageListAdapter extends BaseListAdapter<AVUser> {

  public FollowPageListAdapter(Context ctx) {
    super(ctx);
  }

  @Override
  public View getView(int position, View conView, ViewGroup parent) {
    if (conView == null) {
      LayoutInflater inflater = LayoutInflater.from(ctx);
      conView = inflater.inflate(R.layout.follow_item, null, false);
    }
    ImageView avatarView = findViewById(conView, R.id.avatarView);
    TextView nameView = findViewById(conView, R.id.nameView);

    AVUser user = datas.get(position);
    ImageUtils.displayAvatar(user, avatarView);
    nameView.setText(user.getUsername());
    return conView;
  }
}
