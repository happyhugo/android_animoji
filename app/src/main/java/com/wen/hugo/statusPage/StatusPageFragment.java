package com.wen.hugo.statusPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.wen.hugo.R;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.widget.ListView.BaseListView;

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

    private StatusPageListAdapter adapter;

    @BindView(R.id.comment_List)
    BaseListView<Comment> commentList;

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
        commentList.setToastIfEmpty(false);
        commentList.onRefresh();

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
        adapter = new StatusPageListAdapter(getActivity());
        adapter.setPresenter(mPresenter);
        commentList.init(new BaseListView.DataInterface<Comment>() {
            @Override
            public List<Comment> getDatas(int skip, int limit, List<Comment> currentDatas) throws Exception {
                return mPresenter.getComments(statusId,skip,limit);
            }

            @Override
            public boolean onItemLongPressed(final Comment item) {
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

            @Override
            public void onItemSelected(final Comment item){
                if (item.getFrom().equals(AVUser.getCurrentUser())) {
                    showLoadingError("不自己回复自己");
//                  return;
                }
                if(mComment.getReplayTo()!=null&&!mComment.getReplayTo().equals(item.getFrom())){
                    mEtComment.setText("");
                }

                mEtComment.setHint(String.format("回复 %s 的评论:", item.getFrom().getUsername()));
                mEtComment.requestFocus();

                inputMethodManager.showSoftInput(mEtComment, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                mComment.setReplayTo(item.getFrom());
            }
        }, adapter);
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
    public void adapterChangeItem(Comment comment, boolean add) {
        if(add){
            adapter.getDatas().add(comment);
            mEtComment.setText("");
            mEtComment.setHint("添加评论");
            mComment = new Comment();
        }else{
            adapter.getDatas().remove(comment);
        }
    }

    @Override
    public void setPresenter(StatusPageContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
