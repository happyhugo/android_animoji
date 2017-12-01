package com.wen.hugo.timeLine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wen.hugo.R;
import com.wen.hugo.bean.Status;
import com.wen.hugo.followPage.FollowPageActivity;
import com.wen.hugo.followPage.FollowPageFragment;
import com.wen.hugo.publishStatus.PublishStatusActivity;
import com.wen.hugo.statusPage.StatusPageActivity;
import com.wen.hugo.widget.ListView.BaseListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hugo on 11/22/17.
 */

public class TimeLineFragment extends Fragment implements TimeLineContract.View {

    private TimeLineContract.Presenter mPresenter;

    private TimeLineListAdapter adapter;

    private boolean timeline;

    private static final int SEND_REQUEST = 2;
    @BindView(R.id.status_List)
    BaseListView<Status> statusList;

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
        statusList.setToastIfEmpty(false);
        if(savedInstanceState==null) {
            statusList.onRefresh();
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
                statusList.onRefresh();
            }
        }
    }

    //logout 的 option 没有实现

    private void initList() {
        statusList.init(new BaseListView.DataInterface<Status>() {
            @Override
            public List<Status> getDatas(int skip, int limit, List<Status> currentDatas) throws Exception {
                return mPresenter.getTimeline(skip, limit);
            }

            @Override
            public void onItemSelected(final Status item){
                StatusPageActivity.go(getContext(),item.getStatus().getObjectId());
            }

        }, adapter);
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
    public boolean isTimeLine() {
        return timeline;
    }
}
