package com.wen.hugo.followPage;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;

import java.util.List;

/**
 * Created by hugo on 11/21/17.
 */

public interface FollowPageContract {

    interface View extends BaseView<Presenter> {
        void refresh(boolean like,boolean refresh,boolean end,List<AVUser> data);

        void clear();

        void showLoadingError(String reason);
    }

    interface Presenter extends BasePresenter {

        void getFollows(String userId, int skip);

        void getFollowing(String userId, int skip,boolean force);
    }
}