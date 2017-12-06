package com.wen.hugo.statusPage;

import com.wen.hugo.base.BasePresenter;
import com.wen.hugo.base.BaseView;
import com.wen.hugo.bean.Comment;

import java.util.List;

/**
 * Created by hugo on 11/21/17.
 */

public interface StatusPageContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLoadingError(String reason);

        void refresh(boolean like,boolean refresh,boolean end,List<Comment> data);

        void adapterChangeItem(Comment comment,boolean add);

        void clear();

        void replayComment(Comment item);
    }

    interface Presenter extends BasePresenter {

        void deleteComment(Comment comment);

        void addComment(String statusid,Comment comment);

        void getComments(String statusId, int skip) ;

        void replayComment(Comment item);
    }
}