package com.wen.hugo.timeLine;

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

        void refresh(boolean like,boolean refresh,boolean end,List<Status> data);

        boolean isTimeLine();

        void clear();
    }

    interface Presenter extends BasePresenter {

        void updateStatusLikes(Status status,List<String> likes);

        void getTimeline(int skip);
    }
}