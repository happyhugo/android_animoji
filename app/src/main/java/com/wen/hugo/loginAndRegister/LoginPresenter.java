package com.wen.hugo.loginAndRegister;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
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

public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final LoginContract.View mLoginView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public LoginPresenter( @NonNull DataSource dataRepository,
                                @NonNull LoginContract.View loginView,
                                @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataRepository = dataRepository;
        mLoginView = loginView;

        mSchedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mLoginView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mLoginView.setLoadingIndicator(false);
        mCompositeDisposable.clear();
    }

    @Override
    public void login(final String name,final String password) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            mLoginView.setLoadingIndicator(true);
            mCompositeDisposable.add(
                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> e) throws Exception {
                            try{
                                mDataRepository.login(name,password);
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
                        public void accept(String reason) throws Exception {
                            mLoginView.setLoadingIndicator(false);
                            if(TextUtils.isEmpty(reason)){
                                mLoginView.succeed();
                            }else{
                                mLoginView.showLoadingError(reason);
                            }
                        }
                    }));
        }else{
            mLoginView.showLoadingError("用户名或密码不能为空");
        }
    }
}
