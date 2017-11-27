package com.wen.hugo.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
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

    List<Status> getTimeline(long maxId, int limit) throws AVException;

    List<Status> getUserStatusList(AVUser user, int skip, int limit) throws AVException;

    Observable<List<User>> getFollowers(User user,int skip,int limit);

    Observable<List<User>> getFollowings(User user,int skip,int limit);

    List<Comment> getComments(String statusId,int skip,int limit) throws AVException;

    boolean getRelationship(User user,boolean isFollower);

    Observable<String> addSendStatus(@NonNull String content,Bitmap bitmap);

    void addComment(String statusId,@NonNull Comment comment) throws AVException;

    String addUploadFile(@NonNull Bitmap bitmap) throws AVException;

    void updateFollowAction(User user,boolean follow);

    void updateStatusLikes(Status status,List<String> likes) throws AVException;

    void deleteStatus(Status status) throws AVException;

    void deleteComment(Comment comment) throws AVException;

    void deleteFile(String url);

    void login(String username,String password) throws AVException;

    void signUp(User user);

    boolean IMLogin(String username,String password);

    boolean IMRegister(String username,String password);
}
