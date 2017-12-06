package com.wen.hugo.timeLine;

import android.support.annotation.NonNull;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.bean.Status;
import com.wen.hugo.data.DataSource;
import com.wen.hugo.util.Constans;
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

        mStatus = new ArrayList<>();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
//      mTimeLineView.setLoadingIndicator(false);
        mTimeLineView.clear();
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
                        mDataRepository.updateStatusLikes(status, likes);
                        e.onNext("");
                    }
                })
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reason) {
                        status.setUpdateStatus(false);
                        mStatus.remove(status);
                        mTimeLineView.refresh(true,false,false,null);
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
    public void getTimeline(final int skip){
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<List<Status>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Status>> e) throws Exception {
                            List<Status> temp;
                            if(mTimeLineView.isTimeLine()){
                                temp = mDataRepository.getTimeline(skip, Constans.ONE_PAGE_SIZE);
                            }else {
                                temp = mDataRepository.getNewStatus(skip, Constans.ONE_PAGE_SIZE);
                            }
                            e.onNext(temp);
                    }
                })
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<List<Status>>() {
                    @Override
                    public void accept(List<Status> data) {
                        mTimeLineView.refresh(false,skip==0,data.size()!=Constans.ONE_PAGE_SIZE,data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mTimeLineView.showLoadingError(throwable.getMessage());
                    }
                }));

    }
}
