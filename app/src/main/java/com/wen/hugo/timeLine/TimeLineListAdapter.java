package com.wen.hugo.timeLine;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wen.hugo.R;
import com.wen.hugo.activity.ImageBrowserActivity;
import com.wen.hugo.bean.Status;
import com.wen.hugo.statusPage.StatusPageActivity;
import com.wen.hugo.userPage.UserPageActivity;
import com.wen.hugo.util.ImageUtils;
import com.wen.hugo.util.schedulers.TimeUtils;

import java.util.ArrayList;
import java.util.List;


public class TimeLineListAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    private Context ctx;

    private TimeLineContract.Presenter mPresenter;

    public TimeLineListAdapter(Context ctx,TimeLineContract.Presenter presenter) {
        super( R.layout.status_item, null);
        this.ctx = ctx;
        mPresenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper,final Status status) {
        final AVUser source = status.getUser();
        ImageView avatarView = ((ImageView)helper.getView(R.id.avatarView));
        EaseImageUtils.displayAvatar(source.getUsername(), avatarView,((ImageView)helper.getView(R.id.avatarView2)));
        helper.setText(R.id.nameView,source.getUsername());
        TextView statusText = ((TextView)helper.getView(R.id.statusText));
        if (TextUtils.isEmpty(status.getMessage())) {
            statusText.setVisibility(View.GONE);
        } else {
            statusText.setText(status.getMessage());
            statusText.setVisibility(View.VISIBLE);
        }
        ImageView statusImage = ((ImageView)helper.getView(R.id.statusImage));

        if (TextUtils.isEmpty(status.getImg()) == false) {
            statusImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(status.getImg(),
                    statusImage, ImageUtils.normalImageOptions);
            statusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, ImageBrowserActivity.class);
                    intent.putExtra("url", status.getImg());
                    ctx.startActivity(intent);
                }
            });
        } else {
            statusImage.setVisibility(View.GONE);
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
            helper.setText(R.id.likeCount,n + "");
        } else {
            helper.setText(R.id.likeCount,"");
        }

        final AVUser user = AVUser.getCurrentUser();
        final String userId = user.getObjectId();
        final boolean contains = likes.contains(userId);
        ImageView likeView = ((ImageView)helper.getView(R.id.likeView));
        if (contains) {
            likeView.setImageResource(R.drawable.status_ic_player_liked);
        } else {
            likeView.setImageResource(R.drawable.ic_player_like);
        }

        View likeLayout = ((View)helper.getView(R.id.likeLayout));
        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.updateStatusLikes(status, likes);
            }
        });

        helper.setText(R.id.timeView, TimeUtils.millisecs2DateString(status.getDate().getTime()));

        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPageActivity.go(ctx, source);
            }
        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusPageActivity.go(view.getContext(),status);
            }
        });
    }
}
