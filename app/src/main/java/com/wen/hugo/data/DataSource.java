package com.wen.hugo.data;

import android.support.annotation.NonNull;

import com.wen.hugo.Bean.Comment;
import com.wen.hugo.Bean.Status;
import com.wen.hugo.Bean.User;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by hugo on 11/21/17.
 */

public interface DataSource {

    Flowable<List<Status>> getNewStatus(int skip,int limit);

    Flowable<List<Status>> getTimeline(User my,long maxId,int limit);

    Flowable<List<Status>> getUserStatus(User user,long maxId,int limit);

    Flowable<List<User>> getFollowers(User user,int skip,int limit);

    Flowable<List<User>> getFollowings(User user,int skip,int limit);

    Flowable<List<Comment>> getComments(Status status,int skip,int limit);

    boolean getRelationship(User user,boolean isFollower);

    void addSendStatus(@NonNull Status status);

    void addComment(@NonNull Comment comment);

    String addUploadFile(@NonNull String path);

    void updateFollowAction(User user,boolean follow);

    void updateStatusLikes(Status status,List<String> likes);

    void deleteStatus(Status status);

    void deleteComment(Comment comment);

    void deleteFile(String url);

    User login(User user);

    void signUp(User user);

    boolean IMLogin(String username,String password);

    boolean IMRegister(String username,String password);
}
