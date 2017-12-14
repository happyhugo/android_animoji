package com.wen.hugo.followPage;


import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wen.hugo.R;
import com.wen.hugo.util.ImageUtils;


public class FollowPageListAdapter extends BaseQuickAdapter<AVUser, BaseViewHolder> {

    public FollowPageListAdapter() {
        super(R.layout.follow_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, AVUser item) {
        ImageView avatarView = ((ImageView)helper.getView(R.id.avatarView));
        ImageView avatarView2 = ((ImageView)helper.getView(R.id.avatarView2));
        ImageUtils.displayAvatar(item, avatarView,avatarView2);
        helper.setText(R.id.nameView,item.getUsername());
    }
}
