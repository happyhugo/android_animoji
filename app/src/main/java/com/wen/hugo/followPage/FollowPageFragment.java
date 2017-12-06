package com.wen.hugo.followPage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wen.hugo.R;
import com.wen.hugo.userPage.UserPageActivity;

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

    private FollowPageListAdapter mAdapter;

    private View errorView;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

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
        refresh();
        return root;
    }

    private void initList() {
        Intent intent = getActivity().getIntent();
        type = intent.getIntExtra(TYPE,TYPE_FOLLOWER);

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
    }

    private void initAdapter() {
        mAdapter = new FollowPageListAdapter();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation();
        mAdapter.setEmptyView(errorView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserPageActivity.go(getContext(), (AVUser) adapter.getItem(position));
            }
        });
//      mAdapter.setPreLoadNumber(3);
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
        if(type==TYPE_FOLLOWER) {
            mPresenter.getFollows(AVUser.getCurrentUser().getObjectId(),0);
        }else if(type==TYPE_FOLLOWING) {
            mPresenter.getFollowing(AVUser.getCurrentUser().getObjectId(), 0);
        }
    }

    private void loadMore() {
        mSwipeRefreshLayout.setEnabled(false);
        if(type==TYPE_FOLLOWER) {
            mPresenter.getFollows(AVUser.getCurrentUser().getObjectId(),mAdapter.getData().size());
        }else if(type==TYPE_FOLLOWING) {
            mPresenter.getFollowing(AVUser.getCurrentUser().getObjectId(), mAdapter.getData().size());
        }
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
            mAdapter.loadMoreEnd(isRefresh);
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
    public void refresh(boolean like, boolean refresh, boolean end, List<AVUser> data) {
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
}
