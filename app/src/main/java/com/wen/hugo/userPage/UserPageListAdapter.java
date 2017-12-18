package com.wen.hugo.userPage;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wen.hugo.R;
import com.wen.hugo.activity.ImageBrowserActivity;
import com.wen.hugo.bean.Status;
import com.wen.hugo.statusPage.StatusPageActivity;
import com.wen.hugo.util.ImageUtils;
import com.wen.hugo.util.schedulers.TimeUtils;


public class UserPageListAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    private UserPageContract.Presenter mPresenter;

    private Context ctx;

    public UserPageListAdapter(Context ctx, UserPageContract.Presenter presenter) {
        super(R.layout.user_item, null);
        this.ctx = ctx;
        mPresenter = presenter;
    }

    @Override
    protected void convert(BaseViewHolder helper,final Status status) {

        ImageView statusImage = ((ImageView)helper.getView(R.id.post_attachment));

        if (TextUtils.isEmpty(status.getImg()) == false) {
            statusImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(status.getImg(),statusImage, ImageUtils.normalImageOptions);
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


        TextView statusText = ((TextView)helper.getView(R.id.post_content));
        if (TextUtils.isEmpty(status.getMessage())) {
            statusText.setVisibility(View.GONE);
        } else {
            statusText.setText(status.getMessage());
            statusText.setVisibility(View.VISIBLE);
        }

        helper.setText(R.id.post_time, TimeUtils.millisecs2DateString(status.getDate().getTime()));

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusPageActivity.go(view.getContext(),status);
            }
        });
    }
}
