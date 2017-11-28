package com.wen.hugo.followPage;

import android.support.annotation.NonNull;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wen.hugo.data.DataSource;
import com.wen.hugo.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hugo on 11/22/17.
 */

public class FollowPagePresenter implements FollowPageContract.Presenter {

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final FollowPageContract.View mFollowPageView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;


    public FollowPagePresenter(@NonNull DataSource dataRepository,
                               @NonNull FollowPageContract.View followPageView,
                               @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataRepository = dataRepository;
        mFollowPageView = followPageView;

        mSchedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mFollowPageView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
//      mTimeLineView.setLoadingIndicator(false);
        mCompositeDisposable.clear();
    }

    @Override
    public List<AVUser> getFollows(String userId, int skip, int limit) throws AVException {
        return mDataRepository.getFollows(userId, skip, limit);
    }

    @Override
    public List<AVUser> getFollowing(String userId, int skip, int limit) throws AVException {
        return mDataRepository.getFollowing(userId, skip, limit);
    }
}
