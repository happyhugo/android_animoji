package com.wen.hugo.userPage;

import com.avos.avoscloud.AVException;
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

        void refresh();

        void adapterRemoveItem(Status status);

    }

    interface Presenter extends BasePresenter {

        void updateStatusLikes(Status status,List<String> likes);

        List<Status> getUserStatusList(AVUser user, int skip, int limit) throws AVException;

        void deleteStatus(Status status);

    }
}