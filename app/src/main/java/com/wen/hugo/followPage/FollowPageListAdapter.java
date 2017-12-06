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
        ImageUtils.displayAvatar(item, avatarView);
        helper.setText(R.id.nameView,item.getUsername());
    }
}
