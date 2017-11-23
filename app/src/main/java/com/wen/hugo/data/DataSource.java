package com.wen.hugo.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.avos.avoscloud.AVException;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.bean.Status;
import com.wen.hugo.bean.User;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by hugo on 11/21/17.
 */

public interface DataSource {

    Observable<List<Status>> getNewStatus(int skip, int limit);

    Observable<List<Status>> getTimeline(User my, long maxId, int limit);

    Observable<List<Status>> getUserStatus(User user,long maxId,int limit);

    Observable<List<User>> getFollowers(User user,int skip,int limit);

    Observable<List<User>> getFollowings(User user,int skip,int limit);

    Observable<List<Comment>> getComments(Status status,int skip,int limit);

    boolean getRelationship(User user,boolean isFollower);

    Observable<String> addSendStatus(@NonNull String content,Bitmap bitmap);

    void addComment(@NonNull Comment comment);

    String addUploadFile(@NonNull Bitmap bitmap) throws AVException;

    void updateFollowAction(User user,boolean follow);

    void updateStatusLikes(Status status,List<String> likes);

    void deleteStatus(Status status);

    void deleteComment(Comment comment);

    void deleteFile(String url);

    void login(String username,String password) throws AVException;

    void signUp(User user);

    boolean IMLogin(String username,String password);

    boolean IMRegister(String username,String password);
}
