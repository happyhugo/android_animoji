package com.wen.hugo.subjectPage;

import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;
import com.wen.hugo.bean.Subject;

import java.util.List;

/**
 * Created by hugo on 11/21/17.
 */

public interface SubjectPageContract {

    interface View extends BaseView<Presenter> {

        void showLoadingError(String reason);

        void refresh(boolean like, boolean refresh, boolean end, List<Subject> data);

        void clear();
    }

    interface Presenter extends BasePresenter {

        void getSubjects(int skip) ;

    }
}