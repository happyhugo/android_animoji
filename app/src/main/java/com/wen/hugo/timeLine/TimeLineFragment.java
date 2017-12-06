package com.wen.hugo.timeLine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wen.hugo.R;
import com.wen.hugo.bean.Status;
import com.wen.hugo.publishStatus.PublishStatusActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hugo on 11/22/17.
 */

public class TimeLineFragment extends Fragment implements TimeLineContract.View {

    private TimeLineContract.Presenter mPresenter;

    private TimeLineListAdapter mAdapter;

    private boolean timeline;

    public static final int SEND_REQUEST = 2;

    private View errorView;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static TimeLineFragment newInstance(boolean timeline) {
        TimeLineFragment t = new TimeLineFragment();
        t.setTimeline(timeline);
        return t;
    }

    public void setTimeline(boolean timeline) {
        this.timeline = timeline;
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
    public void setPresenter(@NonNull TimeLineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.timeline_frag, container, false);
        ButterKnife.bind(this, root);
        setRetainInstance(true);

        if(savedInstanceState==null) {
            mAdapter = new TimeLineListAdapter(getActivity(),mPresenter);
        }
        initList();
        if(savedInstanceState==null) {
            refresh();
        }
        return root;
    }

    public void send(){
        Intent intent = new Intent(getActivity(), PublishStatusActivity.class);
        startActivityForResult(intent, SEND_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SEND_REQUEST) {
                refresh();
            }
        }
    }

    //logout 的 option 没有实现
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
        mPresenter.getTimeline(0);
    }

    private void loadMore() {
        mSwipeRefreshLayout.setEnabled(false);
        mPresenter.getTimeline(mAdapter.getData().size());
    }

    private void setData(boolean isRefresh, List data,boolean end) {
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
    public void refresh(boolean like,boolean refresh,boolean end,List<Status> data) {
        if(like){
            mAdapter.notifyDataSetChanged();
            return;
        }

        setData(refresh,data,end);
        if(refresh){
            mAdapter.setEnableLoadMore(true);
            mSwipeRefreshLayout.setRefreshing(false);
        }else{
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public boolean isTimeLine() {
        return timeline;
    }

    @Override
    public void clear() {
        mAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
