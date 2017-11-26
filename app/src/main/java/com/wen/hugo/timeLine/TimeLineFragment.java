package com.wen.hugo.timeLine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVStatus;
import com.wen.hugo.R;
import com.wen.hugo.bean.Status;
import com.wen.hugo.publishStatus.PublishStatusActivity;
import com.wen.hugo.widget.ListView.BaseListView;

import java.util.ArrayList;
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

    private static final int SEND_REQUEST = 2;
    @BindView(R.id.status_List)
    BaseListView<Status> statusList;

    public static TimeLineFragment newInstance() {
        return new TimeLineFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.timeline_frag, container, false);
        ButterKnife.bind(this,root);
        setRetainInstance(true);

        initList();
        statusList.setToastIfEmpty(false);
        statusList.onRefresh();

        return root;
    }

    @OnClick(R.id.send)
    void goSend() {
        Intent intent = new Intent(getActivity(), PublishStatusActivity.class);
        startActivityForResult(intent, SEND_REQUEST);
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
        adapter = new TimeLineListAdapter(getActivity());
        adapter.setPresenter(mPresenter);
        statusList.init(new BaseListView.DataInterface<Status>() {
            @Override
            public List<Status> getDatas(int skip, int limit, List<Status> currentDatas) throws Exception {
                long maxId;
                maxId = getMaxId(skip, currentDatas);
                if (maxId == 0) {
                    return new ArrayList<>();
                } else {
                    return mPresenter.getTimeline(maxId, limit);
                }
            }
        }, adapter);
    }

    private long getMaxId(int skip, List<Status> currentDatas) {
        long maxId;
        if (skip == 0) {
            maxId = Long.MAX_VALUE;
        } else {
            AVStatus lastStatus = currentDatas.get(currentDatas.size() - 1).getInnerStatus();
            maxId = lastStatus.getMessageId() - 1;
        }
        return maxId;
    }

    @Override
    public void showLoadingError(String reason) {
        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }
}
