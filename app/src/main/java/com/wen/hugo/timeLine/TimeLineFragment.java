package com.wen.hugo.timeLine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wen.hugo.R;
import com.wen.hugo.followPage.FollowPageActivity;
import com.wen.hugo.followPage.FollowPageFragment;
import com.wen.hugo.publishStatus.PublishStatusActivity;
import com.wen.hugo.widget.ListView.ProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.fangx.haorefresh.HaoRecyclerView;
import me.fangx.haorefresh.LoadMoreListener;

/**
 * Created by hugo on 11/22/17.
 */

public class TimeLineFragment extends Fragment implements TimeLineContract.View {

    private TimeLineContract.Presenter mPresenter;

    private TimeLineListAdapter adapter;

    private boolean timeline;

    private static final int SEND_REQUEST = 2;

    @BindView(R.id.hao_recycleview)
    HaoRecyclerView recyclerView;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;

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
            adapter = new TimeLineListAdapter(getActivity());
            adapter.setPresenter(mPresenter);
        }
        initList();
        if(savedInstanceState==null) {
            mPresenter.getTimeline(true);
        }
        return root;
    }

    public void send(){
        Intent intent = new Intent(getActivity(), PublishStatusActivity.class);
        startActivityForResult(intent, SEND_REQUEST);
    }

    @OnClick(R.id.followers)
    void goFollowers() {
        FollowPageActivity.goFollow(getContext(),FollowPageFragment.TYPE_FOLLOWER);
    }

    @OnClick(R.id.following)
    void goFollowing() {
        FollowPageActivity.goFollow(getContext(),FollowPageFragment.TYPE_FOLLOWING);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SEND_REQUEST) {
                mPresenter.getTimeline(true);
            }
        }
    }

    //logout 的 option 没有实现
    private void initList() {
        recyclerView.setAdapter(adapter);

//        swiperefresh.setColorSchemeResources(R.color.textBlueDark, R.color.textBlueDark, R.color.textBlueDark,
//                R.color.textBlueDark);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getTimeline(true);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //设置自定义加载中和到底了效果
        ProgressView progressView = new ProgressView(getContext());
        progressView.setIndicatorId(ProgressView.BallPulse);
        progressView.setIndicatorColor(0xff69b3e0);
        recyclerView.setFootLoadingView(progressView);

        TextView textView = new TextView(getContext());
        textView.setText("已经到底啦~");
        recyclerView.setFootEndView(textView);

        recyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getTimeline(false);
            }
        });
    }

    @Override
    public void showLoadingError(String reason) {
        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh(boolean like,boolean refresh,boolean end) {
        if(like){
            adapter.notifyDataSetChanged();
            return;
        }
        if(refresh) {
            recyclerView.refreshComplete();
            recyclerView.loadMoreComplete();
            swiperefresh.setRefreshing(false);
        }else{
            if (end) {
                recyclerView.loadMoreEnd();
            } else {
                recyclerView.loadMoreComplete();
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean isTimeLine() {
        return timeline;
    }
}
