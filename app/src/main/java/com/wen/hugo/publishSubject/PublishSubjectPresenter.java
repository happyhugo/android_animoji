package com.wen.hugo.publishSubject;

import android.support.annotation.NonNull;

import com.wen.hugo.bean.Subject;
import com.wen.hugo.data.DataSource;
import com.wen.hugo.util.schedulers.BaseSchedulerProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by hugo on 11/22/17.
 */

public class PublishSubjectPresenter implements PublishSubjectContract.Presenter {

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final PublishSubjectContract.View mPublishSubejctView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public PublishSubjectPresenter(@NonNull DataSource dataRepository,
                                   @NonNull PublishSubjectContract.View publishSubejctView,
                                   @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataRepository = dataRepository;
        mPublishSubejctView = publishSubejctView;

        mSchedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mPublishSubejctView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mPublishSubejctView.setLoadingIndicator(false);
        mCompositeDisposable.clear();
    }

    @Override
    public void addSubject(final Subject subject) {
            mPublishSubejctView.setLoadingIndicator(true);
            mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> e) throws Exception {
                            mDataRepository.addSubject(subject);
                            e.onNext("");
                        }
                    })
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reason) {
                        mPublishSubejctView.setLoadingIndicator(false);
                        mPublishSubejctView.succeed();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPublishSubejctView.setLoadingIndicator(false);
                        mPublishSubejctView.showLoadingError(throwable.getMessage());
                    }
                }));
    }
}
