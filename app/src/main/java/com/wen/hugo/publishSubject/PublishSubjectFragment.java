package com.wen.hugo.publishSubject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.agora.openvcall.model.Subject;

/**
 * Created by hugo on 11/22/17.
 */

public class PublishSubjectFragment extends Fragment implements PublishSubjectContract.View {

    private PublishSubjectContract.Presenter mPresenter;

    private ProgressDialog mProgressDialog;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.question1)
    EditText question1;

    @BindView(R.id.question2)
    EditText question2;

    @BindView(R.id.question3)
    EditText question3;

    @BindView(R.id.question4)
    EditText question4;

    @BindView(R.id.question5)
    EditText question5;

    public static PublishSubjectFragment newInstance() {
        return new PublishSubjectFragment();
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
    public void setPresenter(@NonNull PublishSubjectContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.publishsubject_frag, container, false);
        ButterKnife.bind(this,root);
        setRetainInstance(true);

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
    public void succeed() {
        Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
        editText.setText("");
        question1.setText("");
        question2.setText("");
        question3.setText("");
        question4.setText("");
        question5.setText("");
    }

    @OnClick(R.id.send)
    void send() {
        Subject subject = new Subject();
        subject.setTitle(editText.getText().toString());
        subject.setUsername(AVUser.getCurrentUser().getUsername());
        List<String> temp = new ArrayList<>();
        if(!TextUtils.isEmpty(question1.getText().toString())){
            temp.add(question1.getText().toString());
        }
        if(!TextUtils.isEmpty(question2.getText().toString())){
            temp.add(question2.getText().toString());
        }
        if(!TextUtils.isEmpty(question3.getText().toString())){
            temp.add(question3.getText().toString());
        }
        if(!TextUtils.isEmpty(question4.getText().toString())){
            temp.add(question4.getText().toString());
        }
        if(!TextUtils.isEmpty(question5.getText().toString())){
            temp.add(question5.getText().toString());
        }
        subject.setContent(temp);
        mPresenter.addSubject(subject);
    }
}
