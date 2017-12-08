package com.wen.hugo.mySubject;

import android.support.annotation.NonNull;

import com.wen.hugo.bean.Subject;
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

public class MySubjectPresenter implements MySubjectContract.Presenter {

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final MySubjectContract.View mMySubjectView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;


    public MySubjectPresenter(@NonNull DataSource dataRepository,
                              @NonNull MySubjectContract.View mySubjectView,
                              @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataRepository = dataRepository;
        mMySubjectView = mySubjectView;

        mSchedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mMySubjectView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mMySubjectView.clear();
        mMySubjectView.setLoadingIndicator(false);
        mCompositeDisposable.clear();
    }

    @Override
    public void deleteSubject(final Subject subject) {
        mMySubjectView.setLoadingIndicator(true);
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        mDataRepository.deleteSubject(subject);
                        e.onNext("");
                    }
                })
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String reason) {
                                mMySubjectView.setLoadingIndicator(false);
                                mMySubjectView.adapterChangeItem(subject);
                                mMySubjectView.refresh(true, false, false, null);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mMySubjectView.setLoadingIndicator(false);
                                mMySubjectView.showLoadingError(throwable.getMessage());
                            }
                        }));
    }

    @Override
    public void getSubjects(final int skip) {
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<List<Subject>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Subject>> e) throws Exception {
                        e.onNext(mDataRepository.getSubjects(skip, Constans.ONE_MY_SUBJECT_PAGE_SIZE));
                    }
                })
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Consumer<List<Subject>>() {
                            @Override
                            public void accept(List<Subject> data) {
                                mMySubjectView.refresh(false, skip == 0, data.size() != Constans.ONE_MY_SUBJECT_PAGE_SIZE, data);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mMySubjectView.showLoadingError(throwable.getMessage());
                            }
                        }));
    }
}
