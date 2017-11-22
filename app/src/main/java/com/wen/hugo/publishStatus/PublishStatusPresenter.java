package com.wen.hugo.publishStatus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wen.hugo.data.DataSource;
import com.wen.hugo.util.schedulers.BaseSchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

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
    public void publish(String content, Bitmap bitmap) {
        if (!TextUtils.isEmpty(content) && bitmap!=null) {
            mPublishStatusView.setLoadingIndicator(true);

//            mCompositeDisposable.add(Flowable.just(new User(name,password))
//                    .map(new Function<User,String>() {
//                        @Override
//                        public String apply(User user) throws Exception {
//                            try{
//                                mDataRepository.login(user.getUserename(),user.getPassword());
//                                return "";
//                            }catch(AVException e){
//                                return e.getMessage();
//                            }
//                        }
//                    })
//                    .subscribeOn(mSchedulerProvider.computation())
//                    .observeOn(mSchedulerProvider.ui())
//                    .subscribe(new Consumer<String>() {
//                        @Override
//                        public void accept(String reason) throws Exception {
//                            mLoginView.setLoadingIndicator(false);
//                            if(TextUtils.isEmpty(reason)){
//                                mLoginView.succeed();
//                            }else{
//                                mLoginView.showLoadingError(reason);
//                            }
//                        }
//                    }));
        }else{
            mPublishStatusView.showLoadingError("内容或图片不能为空");
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
