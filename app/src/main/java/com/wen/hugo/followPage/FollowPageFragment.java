package com.wen.hugo.followPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.R;
import com.wen.hugo.userPage.UserPageActivity;
import com.wen.hugo.widget.ListView.BaseListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hugo on 11/22/17.
 */

public class FollowPageFragment extends Fragment implements FollowPageContract.View {

    public static final String TYPE = "type";
    public static final int TYPE_FOLLOWER = 0;
    public static final int TYPE_FOLLOWING = 1;

    private int type;

    private FollowPageContract.Presenter mPresenter;

    private FollowPageListAdapter adapter;

    @BindView(R.id.follow_List)
    BaseListView<AVUser> followList;

    public static FollowPageFragment newInstance() {
        return new FollowPageFragment();
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
    public void setPresenter(@NonNull FollowPageContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.followpage_frag, container, false);
        ButterKnife.bind(this,root);
        setRetainInstance(true);

        initList();
        followList.setToastIfEmpty(false);
        followList.onRefresh();

        return root;
    }

    private void initList() {
        Intent intent = getActivity().getIntent();
        type = intent.getIntExtra(TYPE,TYPE_FOLLOWER);

        adapter = new FollowPageListAdapter(getActivity());
        followList.init(new BaseListView.DataInterface<AVUser>() {
            @Override
            public List<AVUser> getDatas(int skip, int limit, List<AVUser> currentDatas) throws Exception {
                if(type==TYPE_FOLLOWER) {
                    return mPresenter.getFollows(AVUser.getCurrentUser().getObjectId(),skip,limit);
                }else if(type==TYPE_FOLLOWING) {
                    return mPresenter.getFollowing(AVUser.getCurrentUser().getObjectId(),skip,limit);
                }else{
                    return null;
                }
            }

            @Override
            public void onItemSelected(final AVUser item){
                UserPageActivity.go(getContext(), item);
            }

        }, adapter);
    }
}
