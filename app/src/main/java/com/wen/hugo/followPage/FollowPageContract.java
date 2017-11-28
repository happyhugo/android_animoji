package com.wen.hugo.followPage;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;

import java.util.List;

/**
 * Created by hugo on 11/21/17.
 */

public interface FollowPageContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {

        List<AVUser> getFollows(String userId, int skip, int limit) throws AVException;

        List<AVUser> getFollowing(String userId, int skip, int limit) throws AVException;
    }
}