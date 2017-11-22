package com.wen.hugo.loginAndRegister;

import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;

/**
 * Created by hugo on 11/21/17.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLoadingError(String reason);

        void succeed();
    }

    interface Presenter extends BasePresenter {

        void login(String name,String password);
    }
}