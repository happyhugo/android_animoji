package com.wen.hugo.register;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.R;
import com.wen.hugo.widget.LoadDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hugo on 11/22/17.
 */

public class RegisterFragment extends Fragment implements RegisterContract.View {

    private RegisterContract.Presenter mPresenter;

    @BindView(R.id.reg_username)
    EditText usernameEditText;

    @BindView(R.id.reg_password)
    EditText passwordEditText;

    @BindView(R.id.rg_img_backgroud)
    ImageView mImg_Background;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
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
    public void setPresenter(@NonNull RegisterContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.register_frag, container, false);
        ButterKnife.bind(this,root);
        setRetainInstance(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_anim);
                mImg_Background.startAnimation(animation);
            }
        }, 200);

        if (AVUser.getCurrentUser() != null) {
            succeed();
        }
        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        if(active) {
            LoadDialog.show(getContext());
        }else{
            LoadDialog.dismiss(getContext());
        }
    }

    @Override
    public void showLoadingError(String reason) {
        Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void succeed() {
        login();
    }

    @OnClick(R.id.reg_login)
    void login() {
        getActivity().finish();
    }



    @OnClick(R.id.reg_button)
    void register() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        mPresenter.register(username,password);
    }
}
