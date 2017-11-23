package com.wen.hugo.timeLine;

import android.graphics.Bitmap;
import android.net.Uri;

import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;

/**
 * Created by hugo on 11/21/17.
 */

public interface TimeLineContract {

    interface View extends BaseView<Presenter> {

        void goToUserPage(String userId);

        void setLoadingIndicator(boolean active);

        void showLoadingError(String reason);

        void succeed();

        void addlike();

        void setButtonAndImage(boolean haveImage, Uri uri);
    }

    interface Presenter extends BasePresenter {

        void publish(String content, Bitmap bitmap);

        void result(int requestCode, int resultCode, Uri uri);

        void pickImage();
    }
}