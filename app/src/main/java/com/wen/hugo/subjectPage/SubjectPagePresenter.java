package com.wen.hugo.subjectPage;

import android.support.annotation.NonNull;

import com.wen.hugo.data.DataSource;
import com.wen.hugo.util.Constans;
import com.wen.hugo.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.agora.openvcall.model.Subject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by hugo on 11/22/17.
 */

public class SubjectPagePresenter implements SubjectPageContract.Presenter {

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final SubjectPageContract.View mSubjectPageView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;


    public SubjectPagePresenter(@NonNull DataSource dataRepository,
                                @NonNull SubjectPageContract.View mySubjectView,
                                @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataRepository = dataRepository;
        mSubjectPageView = mySubjectView;

        mSchedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mSubjectPageView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mSubjectPageView.clear();
        mCompositeDisposable.clear();
    }

    @Override
    public void getSubjects(final int skip) {
        mCompositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<List<Subject>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Subject>> e) throws Exception {
                        e.onNext(mDataRepository.getAllSubjects(skip, Constans.ONE_MY_SUBJECT_PAGE_SIZE));
                    }
                })
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(new Consumer<List<Subject>>() {
                            @Override
                            public void accept(List<Subject> data) {
                                mSubjectPageView.refresh(false, skip == 0, data.size() != Constans.ONE_MY_SUBJECT_PAGE_SIZE, data);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mSubjectPageView.showLoadingError(throwable.getMessage());
                            }
                        }));
    }
}
