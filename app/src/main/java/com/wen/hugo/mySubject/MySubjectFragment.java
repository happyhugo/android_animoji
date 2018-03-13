package com.wen.hugo.mySubject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.openvcall.model.Subject;

/**
 * Created by hugo on 11/22/17.
 */

public class MySubjectFragment extends Fragment implements MySubjectContract.View {

    private ProgressDialog mProgressDialog;

    private MySubjectContract.Presenter mPresenter;

    private MySubjectListAdapter mAdapter;

    private View errorView;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    public static MySubjectFragment newInstance() {
        return new MySubjectFragment();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mysubject_frag, container, false);
        ButterKnife.bind(this,root);
        setRetainInstance(true);
        init();
        refresh();
        return root;
    }

    private void init() {
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
        mAdapter = new MySubjectListAdapter();
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
                final Subject item = (Subject) adapter.getItem(position);
                if (item.getFrom().equals(AVUser.getCurrentUser())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("要删除这个问题么?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.deleteSubject(item);
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
        mPresenter.getSubjects(0);
    }

    private void loadMore() {
        mSwipeRefreshLayout.setEnabled(false);
        mPresenter.getSubjects(mAdapter.getData().size());
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
    public void refresh(boolean like, boolean refresh, boolean end, List<Subject> data) {
        if (like) {
            mAdapter.notifyDataSetChanged();
            return;
        }

        List<Subject> list = Subject.getSubjects();
        for(Subject subject: data){
             boolean add = true;
             for(Subject getData: list){
                 if(getData.getObjectId().equals(subject.getObjectId())){
                     add = false;
                     break;
                 }
             }
             if(add){
                 list.add(subject);
             }
        }
        setData(refresh, list, end);
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
    public void adapterChangeItem(Subject subject) {
        mAdapter.getData().remove(subject);
    }

    @Override
    public void setPresenter(MySubjectContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
