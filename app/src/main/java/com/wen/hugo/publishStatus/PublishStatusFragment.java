package com.wen.hugo.publishStatus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wen.hugo.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hugo on 11/22/17.
 */

public class PublishStatusFragment extends Fragment implements PublishStatusContract.View {

    private PublishStatusContract.Presenter mPresenter;

    private ProgressDialog mProgressDialog;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.imageAction)
    Button imageAction;

    Bitmap bitmap;

    public static PublishStatusFragment newInstance() {
        return new PublishStatusFragment();
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
    public void setPresenter(@NonNull PublishStatusContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.publish_frag, container, false);
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
        getActivity().setResult(getActivity().RESULT_OK);
        getActivity().finish();
    }

    @OnClick(R.id.send)
    void send() {
        mPresenter.publish(editText.getText().toString(),bitmap);
    }

    @OnClick(R.id.imageAction)
    void imageAction() {
        mPresenter.pickImage();
    }

    @Override
    public void setButtonAndImage(boolean haveImage,Uri uri) {
        if (haveImage) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            }catch(IOException exception){
                exception.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);

            imageAction.setText("取消图片");
            imageView.setVisibility(View.VISIBLE);
        } else {
            bitmap = null;
            imageAction.setText("添加图片");
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode,data!=null?data.getData():null);
    }

    @Override
    public void pickImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, requestCode);
    }
}
