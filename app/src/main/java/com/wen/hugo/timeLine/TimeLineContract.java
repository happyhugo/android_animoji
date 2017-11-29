package com.wen.hugo.timeLine;

import com.avos.avoscloud.AVException;
import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;
import com.wen.hugo.bean.Status;

import java.util.List;

/**
 * Created by hugo on 11/21/17.
 */

public interface TimeLineContract {

    interface View extends BaseView<Presenter> {

        void showLoadingError(String reason);

        void refresh();
    }

    interface Presenter extends BasePresenter {

        void updateStatusLikes(Status status,List<String> likes);

        List<Status> getTimeline(int skip, int limit)  throws AVException;
    }
}