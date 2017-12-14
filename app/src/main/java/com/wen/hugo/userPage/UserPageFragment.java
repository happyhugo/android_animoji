package com.wen.hugo.userPage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.easeui.EaseConstant;
import com.wen.hugo.R;
import com.wen.hugo.bean.Status;
import com.wen.hugo.chatPage.ChatActivity;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hugo on 11/22/17.
 */

public class UserPageFragment extends Fragment implements UserPageContract.View {

    public static final int MUTUAL_FOLLOW = 0;//disable follow
    public static final int FOLLOWER = 1;  //can follow
    public static final int FOLLOWING = 2;  //disable follow
    public static final int NONE_FOLLOW = 3; //can follow

    public static final int CANCEL_FOLLOW = 0;
    public static final int FOLLOW = 1;

    public static final String FOLLOWERss = "follower";
    public static final String FOLLOWEEss = "followee";

    public static final String USER = "user";

    private AVUser user;

    private boolean myself;

    private int followStatus;

    private int actionType = -1;

    private ProgressDialog mProgressDialog;

    private UserPageContract.Presenter mPresenter;

    private UserPageListAdapter mAdapter;

    private View errorView;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.icon)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.user_bottom_frame)
    LinearLayout bottom;

    @BindView(R.id.user_bottom_follow_txt)
    TextView tv;

    public static UserPageFragment newInstance() {
        return new UserPageFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull UserPageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.userpage_frag, container, false);
        ButterKnife.bind(this, root);
        setRetainInstance(true);


        if (savedInstanceState == null) {
            init();
            mAdapter = new UserPageListAdapter(getActivity(), mPresenter);
        }



        initList();

        final Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) root.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(user.getUsername());

        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getActivity().getResources().getColor(ImageUtils.getRandomColor(user.getUsername()))));
        floatingActionButton.setImageResource(ImageUtils.getRandomDrawable(user.getUsername()));

        final ImageView imageView = (ImageView) root.findViewById(R.id.backdrop);
        Glide.with(this).load(ImageUtils.getRandomCheeseDrawable()).centerCrop().into(imageView);

        //保存要保存的view变量  就是refreshs获得的变量
        if (savedInstanceState == null) {
            refresh();
        }

        refreshs(false);
        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }

        if (mProgressDialog == null) {
            if (!active) {
                return;
            }
            mProgressDialog = ProgressDialog.show(getContext(), "标题", "加载中，请稍后……");
            mProgressDialog.setMessage("waiting....");
            mProgressDialog.setCancelable(false);
        }
        if (active) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void adapterRemoveItem(Status status) {
        mAdapter.getData().remove(status);
    }

    private void init() {
        Intent intent = getActivity().getIntent();
        AVUser other = intent.getParcelableExtra(USER);
        AVUser currentUser = AVUser.getCurrentUser();
        if (other == null || currentUser.equals(other)) {
            myself = true;
            user = currentUser;
        } else {
            user = other;
        }
    }

    private void initList() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        errorView = getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) mRecyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        initAdapter();
        initRefreshLayout();


//        @Override
//        public boolean onItemLongPressed(final Status item) {
//            AVUser source = item.getUser();
//            if (source.getObjectId().equals(AVUser.getCurrentUser().getObjectId())) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("要删除这条状态么?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mPresenter.deleteStatus(item);
//                    }
//                }).setNegativeButton("取消", null);
//                builder.show();
//            }
//            return true;
//        }
    }

    private void initAdapter() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation();
        mAdapter.setEmptyView(errorView);
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                final Status item = (Status)adapter.getItem(position);
                AVUser source = item.getUser();
                if (source.getObjectId().equals(AVUser.getCurrentUser().getObjectId())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("要删除这条状态么?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.deleteStatus(item);
                        }
                    }).setNegativeButton("取消", null);
                    builder.show();
                }
                return true;
            }
        });
//        mAdapter.setPreLoadNumber(3);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mPresenter.getUserStatusList(user, 0);
    }

    private void loadMore() {
        mSwipeRefreshLayout.setEnabled(false);
        mPresenter.getUserStatusList(user, mAdapter.getData().size());
    }

    private void setData(boolean isRefresh, List data, boolean end) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (end) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(false);
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void showLoadingError(String reason) {
        clear();
        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh(boolean like, boolean refresh, boolean end, List<Status> data) {
        if (like) {
            mAdapter.notifyDataSetChanged();
            return;
        }

        setData(refresh, data, end);
        if (refresh) {
            mAdapter.setEnableLoadMore(true);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void clear() {
        mAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setText(List<AVUser> avUsers) {
        setLoadingIndicator(false);
        boolean follow = false;
        for(AVUser users:avUsers){
            if(users.getObjectId().equals(user.getObjectId())){
                follow = true;
                break;
            }
        }

        int followButtonResId;
        if (follow) {
            actionType = CANCEL_FOLLOW;
            followButtonResId = R.string.status_cancelFollow;
        } else {
            actionType = FOLLOW;
            followButtonResId = R.string.status_follow;
        }

        tv.setText(getString(followButtonResId));
    }

    @OnClick(R.id.user_bottom_follow)
    void followAction() {
        if (actionType != -1) {
            if (myself) {
                return;
            }
            boolean follow;
            if (actionType == FOLLOW) {
                follow = true;
            } else {
                follow = false;
            }
            followAction(user, follow, new FollowCallback() {

                @Override
                public void done(AVObject object, AVException e) {
                    if (ActivityUtils.filterException(getContext(), e)) {
                        refreshs(true);
                    }
                }
            });
        }
    }

    @OnClick(R.id.user_bottom_chat)
    void talkAction() {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
        startActivity(intent);
    }

    public  void followAction(AVUser user, boolean follow, FollowCallback followCallback) {
        AVUser currentUser = AVUser.getCurrentUser();
        setLoadingIndicator(true);
        if (follow) {
            currentUser.followInBackground(user.getObjectId(), followCallback);
        } else {
            currentUser.unfollowInBackground(user.getObjectId(), followCallback);
        }
    }

    public static int followStatus(AVUser user) throws AVException {
        boolean isMyFollower = findFollowStatus(user, true);
        boolean isMyFollowing = findFollowStatus(user, false);
        if (isMyFollower && isMyFollowing) {
            return MUTUAL_FOLLOW;
        } else if (isMyFollower) {
            return FOLLOWER;
        } else if (isMyFollowing) {
            return FOLLOWING;
        } else {
            return NONE_FOLLOW;
        }
    }

    public static boolean findFollowStatus(AVUser user, boolean askFollower) throws AVException {
        AVUser currentUser = AVUser.getCurrentUser();
        AVQuery<AVUser> q;
        if (askFollower) {
            q = AVUser.followerQuery(currentUser.getObjectId(), AVUser.class);
            q.whereEqualTo(FOLLOWERss, user);
        } else {
            q = AVUser.followeeQuery(currentUser.getObjectId(), AVUser.class);
            q.whereEqualTo(FOLLOWEEss, user);
        }
        q.setLimit(1);
        List<AVUser> avUsers = q.find();
        return avUsers.isEmpty() == false;
    }

    private void refreshs(boolean force) {
        if (myself) {
            bottom.setVisibility(View.GONE);
            return;
        }
        mPresenter.getFollowing(AVUser.getCurrentUser().getObjectId(),0,force);
    }
//        new StatusNetAsyncTask(getActivity()) {
//            @Override
//            protected void doInBack() throws Exception {
//                if (!myself) {
//                    followStatus = followStatus(user);
//                }
//            }
//
//            @Override
//            protected void onPost(Exception e) {
//                if (ActivityUtils.filterException(getActivity(), e)) {
//                    if (myself) {
//                        bottom.setVisibility(View.GONE);
//                        return;
//                    }
//                    bottom.setVisibility(View.VISIBLE);
//
////                    int followStatusDescId = R.string.status_none_follow_desc;
////                    switch (followStatus) {
////                        case MUTUAL_FOLLOW:
////                            followStatusDescId = R.string.status_mutual_follow;
////                            break;
////                        case FOLLOWER:
////                            followStatusDescId = R.string.status_follower_desc;
////                            break;
////                        case FOLLOWING:
////                            followStatusDescId = R.string.status_following_desc;
////                            break;
////                        case NONE_FOLLOW:
////                            followStatusDescId = R.string.status_none_follow_desc;
////                            break;
////                    }
////                    String followStatusDesc = getString(followStatusDescId);
////                    tv.setText(followStatusDesc);
//
//                    int followButtonResId;
//                    if (followStatus == MUTUAL_FOLLOW ||
//                            followStatus == FOLLOWING) {
//                        actionType = CANCEL_FOLLOW;
//                        followButtonResId = R.string.status_cancelFollow;
//                    } else {
//                        actionType = FOLLOW;
//                        followButtonResId = R.string.status_follow;
//                    }
//
//                    tv.setText(getString(followButtonResId));
//                }
//            }
//        }.execute();
//    }
}
