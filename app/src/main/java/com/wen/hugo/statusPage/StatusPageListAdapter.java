package com.wen.hugo.statusPage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.wen.hugo.R;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.userPage.UserPageActivity;
import com.wen.hugo.util.schedulers.TimeUtils;

public class StatusPageListAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    private StatusPageContract.Presenter mPresenter;

    private Context ctx;

    public StatusPageListAdapter(Context ctx, StatusPageContract.Presenter presenter) {
        super(R.layout.comment_item, null);
        this.ctx = ctx;
        mPresenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, final Comment comment) {
        final AVUser source = comment.getFrom();
        ImageView avatarView = ((ImageView)helper.getView(R.id.iv_avatar));
        ImageView avatarView2 = ((ImageView)helper.getView(R.id.avatarView2));
        EaseImageUtils.displayAvatar(source.getUsername(), avatarView,avatarView2);
        helper.setText(R.id.tv_nick,source.getUsername());
        helper.setText(R.id.tv_host,comment.getNumber()+"楼");
        if (comment.getComment().getCreatedAt() == null) {
            helper.setText(R.id.tv_time, TimeUtils.millisecs2DateString(System.currentTimeMillis()));
        } else {
            helper.setText(R.id.tv_time,TimeUtils.millisecs2DateString(comment.getComment().getCreatedAt().getTime()));
        }
        if (comment.getReplayTo() == null) {
            helper.setText(R.id.tv_comment,String.format("回复 楼主 的评论：%s", comment.getComment().get(Comment.CONTENT).toString()));
        } else {
            helper.setText(R.id.tv_comment,String.format("回复 %s 的评论：%s", comment.getReplayTo().getUsername(), comment.getComment().get(Comment.CONTENT).toString()));
        }
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPageActivity.go(ctx, source);
            }
        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.replayComment(comment);
            }
        });
    }
}
