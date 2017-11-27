package com.wen.hugo.statusPage;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.data.DataSource;
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

public class StatusPagePresenter implements StatusPageContract.Presenter {

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final StatusPageContract.View mStatusPageView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;


    public StatusPagePresenter(@NonNull DataSource dataRepository,
                               @NonNull StatusPageContract.View statusPageView,
                               @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataRepository = dataRepository;
        mStatusPageView = statusPageView;

        mSchedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mStatusPageView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mStatusPageView.setLoadingIndicator(false);
        mCompositeDisposable.clear();
    }

    @Override
    public void deleteComment(final Comment comment) {
        mStatusPageView.setLoadingIndicator(true);
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                    try{
                        mDataRepository.deleteComment(comment);
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
                    mStatusPageView.setLoadingIndicator(false);
                    if (TextUtils.isEmpty(reason)) {
                        mStatusPageView.adapterChangeItem(comment,false);
                        mStatusPageView.refresh();
                    }else{
                        mStatusPageView.showLoadingError(reason);
                    }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    mStatusPageView.setLoadingIndicator(false);
                    mStatusPageView.showLoadingError(throwable.getMessage());
                    }
                }));
    }

    @Override
    public void addComment(final String statusId,final Comment comment) {
        mStatusPageView.setLoadingIndicator(true);
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        try{
                            mDataRepository.addComment(statusId,comment);
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
                        mStatusPageView.setLoadingIndicator(false);
                        if (TextUtils.isEmpty(reason)) {
                            mStatusPageView.adapterChangeItem(comment,true);
                            mStatusPageView.refresh();
                        }else{
                            mStatusPageView.showLoadingError(reason);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mStatusPageView.setLoadingIndicator(false);
                        mStatusPageView.showLoadingError(throwable.getMessage());
                    }
                }));
    }

    @Override
    public List<Comment> getComments(String statusId, int skip, int limit) throws AVException {
        return mDataRepository.getComments(statusId, skip, limit);
    }
}
