package com.wen.hugo.publishSubject;

import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;
import com.wen.hugo.bean.Subject;

/**
 * Created by hugo on 11/21/17.
 */

public interface PublishSubjectContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLoadingError(String reason);

        void succeed();

    }

    interface Presenter extends BasePresenter {

        void addSubject(Subject subject);

    }
}