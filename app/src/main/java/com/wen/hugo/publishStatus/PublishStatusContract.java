package com.wen.hugo.publishStatus;

import android.graphics.Bitmap;
import android.net.Uri;

import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;

/**
 * Created by hugo on 11/21/17.
 */

public interface PublishStatusContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLoadingError(String reason);

        void succeed();

        void pickImage(int requestCode);

        void setButtonAndImage(boolean haveImage,Uri uri);
    }

    interface Presenter extends BasePresenter {

        void publish(String content, Bitmap bitmap);

        void result(int requestCode, int resultCode, Uri uri);

        void pickImage();
    }
}