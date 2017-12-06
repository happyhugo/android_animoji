package com.wen.hugo.userPage;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;
import com.wen.hugo.bean.Status;

import java.util.List;

/**
 * Created by hugo on 11/21/17.
 */

public interface UserPageContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLoadingError(String reason);

        void refresh(boolean like,boolean refresh,boolean end,List<Status> data);

        void adapterRemoveItem(Status status);

        void clear();
    }

    interface Presenter extends BasePresenter {

        void updateStatusLikes(Status status,List<String> likes);

        void getUserStatusList(AVUser avUser,int skip);

        void deleteStatus(Status status);

    }
}