package com.wen.hugo.followPage;

import android.support.annotation.NonNull;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.data.DataSource;
import com.wen.hugo.util.Constans;
import com.wen.hugo.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

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
        mFollowPageView.clear();
        mCompositeDisposable.clear();
    }

    @Override
    public void getFollows(final String userId,final int skip){
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<List<AVUser>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<AVUser>> e) throws Exception {
                        e.onNext(mDataRepository.getFollows(userId, skip, Constans.ONE_FOLLOW_PAGE_SIZE));
                    }
                })
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Consumer<List<AVUser>>() {
                            @Override
                            public void accept(List<AVUser> data) {
                                mFollowPageView.refresh(false,skip==0,data.size()!=Constans.ONE_PAGE_SIZE,data);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mFollowPageView.showLoadingError(throwable.getMessage());
                            }
                        }));

    }

    @Override
    public void getFollowing(final String userId,final int skip,final boolean force) {
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<List<AVUser>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<AVUser>> e) throws Exception {
                        e.onNext(mDataRepository.getFollowing(userId, skip, Constans.ONE_FOLLOW_PAGE_SIZE,force));
                    }
                })
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Consumer<List<AVUser>>() {
                            @Override
                            public void accept(List<AVUser> data) {
                                mFollowPageView.refresh(false,skip==0,data.size()!=Constans.ONE_PAGE_SIZE,data);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mFollowPageView.showLoadingError(throwable.getMessage());
                            }
                        }));
    }
}
