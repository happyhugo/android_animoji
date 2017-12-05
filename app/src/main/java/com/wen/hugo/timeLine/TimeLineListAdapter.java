package com.wen.hugo.timeLine;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wen.hugo.R;
import com.wen.hugo.activity.ImageBrowserActivity;
import com.wen.hugo.bean.Status;
import com.wen.hugo.statusPage.StatusPageActivity;
import com.wen.hugo.userPage.UserPageActivity;
import com.wen.hugo.util.ImageUtils;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TimeLineListAdapter extends RecyclerView.Adapter<TimeLineListAdapter.ViewHolder> {

    public List<Status> datas = null;

    private Context ctx;

    private TimeLineContract.Presenter mPresenter;

    public TimeLineListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setPresenter(@NonNull TimeLineContract.Presenter presenter) {
        mPresenter = presenter;
        datas = mPresenter.getData();
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.status_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Status status = datas.get(position);
        final AVUser source = status.getUser();
        ImageUtils.displayAvatar(source, viewHolder.avatarView);
        viewHolder.nameView.setText(source.getUsername());
        if (TextUtils.isEmpty(status.getMessage())) {
            viewHolder.textView.setVisibility(View.GONE);
        } else {
            viewHolder.textView.setText(status.getMessage());
            viewHolder.textView.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(status.getImg()) == false) {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(status.getImg(),
                    viewHolder.imageView, ImageUtils.normalImageOptions);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, ImageBrowserActivity.class);
                    intent.putExtra("url", status.getImg());
                    ctx.startActivity(intent);
                }
            });
        } else {
            viewHolder.imageView.setVisibility(View.GONE);
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
            viewHolder.likeCountView.setText(n + "");
        } else {
            viewHolder.likeCountView.setText("");
        }

        final AVUser user = AVUser.getCurrentUser();
        final String userId = user.getObjectId();
        final boolean contains = likes.contains(userId);
        if (contains) {
            viewHolder.likeView.setImageResource(R.drawable.status_ic_player_liked);
        } else {
            viewHolder.likeView.setImageResource(R.drawable.ic_player_like);
        }
        viewHolder.likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.updateStatusLikes(status, likes);
            }
        });

        viewHolder.timeView.setText(millisecs2DateString(status.getDate().getTime()));


        viewHolder.avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPageActivity.go(ctx, source);
            }
        });
        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusPageActivity.go(view.getContext(),status.getStatus().getObjectId());
            }
        });
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView textView;
        public ImageView avatarView;
        public ImageView imageView;
        public ImageView likeView;
        public TextView likeCountView;
        public View likeLayout;
        public TextView timeView;
        public View myView;

        public ViewHolder(View view) {
            super(view);
            myView = view;
            nameView = view.findViewById(R.id.nameView);
            textView = view.findViewById(R.id.statusText);
            avatarView = view.findViewById(R.id.avatarView);
            imageView = view.findViewById(R.id.statusImage);
            likeView = view.findViewById(R.id.likeView);
            likeCountView = view.findViewById(R.id.likeCount);
            likeLayout = view.findViewById(R.id.likeLayout);
            timeView = view.findViewById(R.id.timeView);

        }
    }
}
