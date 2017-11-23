package com.wen.hugo.publishStatus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wen.hugo.data.DataSource;
import com.wen.hugo.util.schedulers.BaseSchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by hugo on 11/22/17.
 */

public class PublishStatusPresenter implements PublishStatusContract.Presenter {

    public boolean haveImage;

    private static final int IMAGE_PICK_REQUEST = 10;

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final PublishStatusContract.View mPublishStatusView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public PublishStatusPresenter(@NonNull DataSource dataRepository,
                                  @NonNull PublishStatusContract.View publishStatusView,
                                  @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataRepository = dataRepository;
        mPublishStatusView = publishStatusView;

        mSchedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mPublishStatusView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mPublishStatusView.setLoadingIndicator(false);
        mCompositeDisposable.clear();
    }

    @Override
    public void publish(final String content,final Bitmap bitmap) {
        if (!TextUtils.isEmpty(content)) {
            mPublishStatusView.setLoadingIndicator(true);
            mCompositeDisposable.add(
                mDataRepository.addSendStatus(content,bitmap)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reason) {
                        mPublishStatusView.setLoadingIndicator(false);
                        if (TextUtils.isEmpty(reason)) {
                            mPublishStatusView.succeed();
                        } else {
                            mPublishStatusView.showLoadingError(reason);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPublishStatusView.setLoadingIndicator(false);
                        mPublishStatusView.showLoadingError(throwable.getMessage());
                    }
                }));
        }else{
            mPublishStatusView.showLoadingError("内容不能为空");
        }
    }

    @Override
    public void result(int requestCode, int resultCode,Uri uri) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_REQUEST) {
            haveImage = true;
            mPublishStatusView.setButtonAndImage(haveImage,uri);
        }
    }

    @Override
    public void pickImage() {
        if (haveImage == false) {
            mPublishStatusView.pickImage(IMAGE_PICK_REQUEST);
        } else {
            haveImage = false;
            mPublishStatusView.setButtonAndImage(haveImage,null);
        }
    }
}
