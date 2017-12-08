package com.wen.hugo.mySubject;

import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;
import com.wen.hugo.bean.Subject;

import java.util.List;

/**
 * Created by hugo on 11/21/17.
 */

public interface MySubjectContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLoadingError(String reason);

        void refresh(boolean like, boolean refresh, boolean end, List<Subject> data);

        void adapterChangeItem(Subject subject);

        void clear();
    }

    interface Presenter extends BasePresenter {

        void deleteSubject(Subject subject);

        void getSubjects(int skip) ;

    }
}