package com.wen.hugo.statusPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wen.hugo.R;
import com.wen.hugo.bean.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hugo on 11/22/17.
 */

public class StatusPageFragment extends Fragment implements StatusPageContract.View {

    public static final String STATUS_ID = "statusId";

    private ProgressDialog mProgressDialog;

    private StatusPageContract.Presenter mPresenter;

    private StatusPageListAdapter mAdapter;

    private View errorView;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.et_comment)
    EditText mEtComment;

    @BindView(R.id.btn_send)
    Button mBtnSend;

    InputMethodManager inputMethodManager;

    private Comment mComment;

    private String statusId;

    public static StatusPageFragment newInstance() {
        return new StatusPageFragment();
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
        View root = inflater.inflate(R.layout.statuspage_frag, container, false);
        ButterKnife.bind(this,root);
        setRetainInstance(true);
        init();
        refresh();
        return root;
    }

    @OnClick(R.id.btn_send)
    void sendComment() {
        mComment.setContent(mEtComment.getText().toString());
        mComment.setFrom(AVUser.getCurrentUser());
        mPresenter.addComment(statusId,mComment);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void init() {
        Intent intent = getActivity().getIntent();
        statusId = intent.getStringExtra(STATUS_ID);
        mComment = new Comment();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mEtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              if (TextUtils.isEmpty(mEtComment.getText().toString())) {
                  mBtnSend.setEnabled(false);
              } else {
                  mBtnSend.setEnabled(true);
              }
            }
        });
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
        mAdapter = new StatusPageListAdapter(getActivity(),mPresenter);
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
                final Comment item = (Comment) adapter.getItem(position);
                if (item.getFrom().equals(AVUser.getCurrentUser())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("要删除这条评论么?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.deleteComment(item);
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
        mPresenter.getComments(statusId,0);
    }

    private void loadMore() {
        mSwipeRefreshLayout.setEnabled(false);
        mPresenter.getComments(statusId, mAdapter.getData().size());
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
    public void refresh(boolean like, boolean refresh, boolean end, List<Comment> data) {
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
    public void adapterChangeItem(Comment comment, boolean add) {
        if(add){
            mAdapter.getData().add(comment);
            mEtComment.setText("");
            mEtComment.setHint("添加评论");
            mComment = new Comment();
        }else{
            mAdapter.getData().remove(comment);
        }
    }

    @Override
    public void replayComment(Comment item) {
        if (item.getFrom().equals(AVUser.getCurrentUser())) {
            showLoadingError("不自己回复自己");
            return;
        }
        if (mComment.getReplayTo() != null && !mComment.getReplayTo().equals(item.getFrom())) {
            mEtComment.setText("");
        }

        mEtComment.setHint(String.format("回复 %s 的评论:", item.getFrom().getUsername()));
        mEtComment.requestFocus();

        inputMethodManager.showSoftInput(mEtComment, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        mComment.setReplayTo(item.getFrom());
    }

    @Override
    public void setPresenter(StatusPageContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
