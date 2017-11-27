package com.wen.hugo.timeLine;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wen.hugo.bean.Status;
import com.wen.hugo.data.DataSource;
import com.wen.hugo.util.schedulers.BaseSchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by hugo on 11/22/17.
 */

public class TimeLinePresenter implements TimeLineContract.Presenter {

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final TimeLineContract.View mTimeLineView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    private List<Status> mStatus;

    public TimeLinePresenter(@NonNull DataSource dataRepository,
                             @NonNull TimeLineContract.View timeLineView,
                             @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataRepository = dataRepository;
        mTimeLineView = timeLineView;

        mSchedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mTimeLineView.setPresenter(this);

        mStatus = new ArrayList<Status>();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
//      mTimeLineView.setLoadingIndicator(false);
        for(Status status:mStatus){
            status.setUpdateStatus(false);
        }
        mCompositeDisposable.clear();
    }

    @Override
    public void updateStatusLikes(final Status status, final List<String> likes) {
        if(status.isUpdateStatus()){
            return;
        }else{
            status.setUpdateStatus(true);
            mStatus.add(status);
        }
        final String userId = AVUser.getCurrentUser().getObjectId();
        final boolean contains = likes.contains(userId);
        if (contains) {
            likes.remove(userId);
        } else {
            likes.add(userId);
        }
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        try{
                            mDataRepository.updateStatusLikes(status, likes);
                            e.onNext("");
                        }catch(AVException exception){
                            e.onNext(exception.getMessage());
                        }
                    }
                })
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reason) {
                        status.setUpdateStatus(false);
                        mStatus.remove(status);
                        if (TextUtils.isEmpty(reason)) {
                            mTimeLineView.refresh();
                        }else{
                            if (!contains) {
                                likes.remove(userId);
                            } else {
                                likes.add(userId);
                            }
                            mTimeLineView.showLoadingError(reason);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        status.setUpdateStatus(false);
                        mStatus.remove(status);
                        if (!contains) {
                            likes.remove(userId);
                        } else {
                            likes.add(userId);
                        }
                        mTimeLineView.showLoadingError(throwable.getMessage());
                    }
                }));
    }

    @Override
    public List<Status> getTimeline(long maxId, int limit) throws AVException {
        return mDataRepository.getTimeline(maxId, limit);
    }
}
