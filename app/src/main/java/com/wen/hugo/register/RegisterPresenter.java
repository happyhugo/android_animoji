package com.wen.hugo.register;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
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

public class RegisterPresenter implements RegisterContract.Presenter {

    @NonNull
    private final DataSource mDataRepository;

    @NonNull
    private final RegisterContract.View mLoginView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public RegisterPresenter(@NonNull DataSource dataRepository,
                             @NonNull RegisterContract.View loginView,
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
    public void register(final String name,final String password) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            mLoginView.setLoadingIndicator(true);

            mCompositeDisposable.add(
                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> e) throws Exception {
                            try{
                                mDataRepository.register(name,password);
                                EMClient.getInstance().createAccount(name,password);
                                e.onNext("");
                            }catch(AVException exception){
                                e.onNext(exception.getMessage());
                            }catch (HyphenateException exception){
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
                            AVUser.logOut();
                            if(TextUtils.isEmpty(reason)){
                                mLoginView.showLoadingError("注册成功");
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
