package com.wen.hugo.userPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.wen.hugo.R;
import com.wen.hugo.bean.Status;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.widget.ListView.BaseListView;
import com.wen.hugo.widget.ListView.StatusNetAsyncTask;

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

    public static final String USER_ID = "userId";

    private AVUser user;

    private boolean myself;

    private int followStatus;

    private int actionType = -1;

    private ProgressDialog mProgressDialog;

    private UserPageContract.Presenter mPresenter;

    private UserPageListAdapter adapter;

    @BindView(R.id.status_List)
    BaseListView<Status> statusList;

    @BindView(R.id.followAction)
    Button followActionBtn;

    @BindView(R.id.followStatus)
    TextView followStatusView;

    @BindView(R.id.followLayout)
    View followLayout;

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
        ButterKnife.bind(this,root);
        setRetainInstance(true);

        init();
        initList();
        statusList.onRefresh();
        refreshs();

        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }

        if (mProgressDialog == null) {
            if(!active){
                return;
            }
            mProgressDialog = ProgressDialog.show(getContext(), "标题", "加载中，请稍后……");
            mProgressDialog.setMessage("waiting....");
            mProgressDialog.setCancelable(false);
        }
        if(active) {
            mProgressDialog.show();
        }else{
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showLoadingError(String reason) {
        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void adapterRemoveItem(Status status) {
        adapter.getDatas().remove(status);
    }

    private void init() {
        Intent intent = getActivity().getIntent();
        String userId = intent.getStringExtra(USER_ID);
        AVUser currentUser = AVUser.getCurrentUser();
        myself = userId.equals(currentUser.getObjectId());
        if(myself){
            user = currentUser;
        }
    }

    private void initList() {
        adapter = new UserPageListAdapter(getActivity());
        adapter.setPresenter(mPresenter);
        statusList.init(new BaseListView.DataInterface<Status>() {
            public List<Status> getDatas(int skip, int limit, List<Status> currentDatas) throws Exception {
                return mPresenter.getUserStatusList(user, skip, limit);
            }

            @Override
            public void onItemLongPressed(final Status item) {
                AVStatus innerStatus = item.getInnerStatus();
                AVUser source = innerStatus.getSource();
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
            }

        }, adapter);
        statusList.setToastIfEmpty(false);
    }

    @OnClick(R.id.followAction)
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
                    if (ActivityUtils.filterException(getActivity(), e)) {
                        refreshs();
                    }
                }
            });
        }
    }

    public static void followAction(AVUser user, boolean follow, FollowCallback followCallback) {
        AVUser currentUser = AVUser.getCurrentUser();
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

    private void refreshs() {
        new StatusNetAsyncTask(getActivity()) {
            @Override
            protected void doInBack() throws Exception {
                if (!myself) {
                    followStatus = followStatus(user);
                }
            }

            @Override
            protected void onPost(Exception e) {
                if (ActivityUtils.filterException(getActivity(), e)) {
                    if (myself) {
                        followStatusView.setVisibility(View.GONE);
                        followLayout.setVisibility(View.GONE);
                        followActionBtn.setVisibility(View.GONE);
                        return;
                    }
                    followStatusView.setVisibility(View.VISIBLE);
                    followActionBtn.setVisibility(View.VISIBLE);

                    int followStatusDescId = R.string.status_none_follow_desc;
                    switch (followStatus) {
                        case MUTUAL_FOLLOW:
                            followStatusDescId = R.string.status_mutual_follow;
                            break;
                        case FOLLOWER:
                            followStatusDescId = R.string.status_follower_desc;
                            break;
                        case FOLLOWING:
                            followStatusDescId = R.string.status_following_desc;
                            break;
                        case NONE_FOLLOW:
                            followStatusDescId = R.string.status_none_follow_desc;
                            break;
                    }
                    String followStatusDesc = getString(followStatusDescId);
                    followStatusView.setText(followStatusDesc);

                    int followButtonResId;
                    if (followStatus == MUTUAL_FOLLOW ||
                            followStatus == FOLLOWING) {
                        actionType = CANCEL_FOLLOW;
                        followButtonResId = R.string.status_cancelFollow;
                    } else {
                        actionType = FOLLOW;
                        followButtonResId = R.string.status_follow;
                    }

                    followActionBtn.setText(getString(followButtonResId));
                }
            }
        }.execute();
    }
}
