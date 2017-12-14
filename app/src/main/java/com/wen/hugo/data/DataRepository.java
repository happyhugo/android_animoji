package com.wen.hugo.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.bean.Status;
import com.wen.hugo.bean.Subject;
import com.wen.hugo.bean.User;
import com.wen.hugo.data.local.LocalDataSource;
import com.wen.hugo.data.remote.AVRemoteDataSource;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by hugo on 11/21/17.
 */

public class DataRepository implements DataSource{
    @Nullable
    private static DataRepository INSTANCE = null;

    @NonNull
    private final DataSource mAVRemoteDataSource;

    @NonNull
    private final DataSource mLocalDataSource;

    private DataRepository(@NonNull DataSource tasksRemoteDataSource,
                            @NonNull DataSource tasksLocalDataSource) {
        mAVRemoteDataSource = tasksRemoteDataSource;
        mLocalDataSource = tasksLocalDataSource;
    }

    public static DataRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(AVRemoteDataSource.getInstance(), new LocalDataSource());
        }
        return INSTANCE;
    }

    @Override
    public List<Status> getNewStatus(int skip, int limit) throws AVException {
        return mAVRemoteDataSource.getNewStatus(skip,limit);
    }

    @Override
    public List<Status> getTimeline(int skip, int limit) throws AVException {
        return mAVRemoteDataSource.getTimeline(skip,limit);
    }

    @Override
    public List<Status> getUserStatusList(AVUser user, int skip, int limit) throws AVException{
        return mAVRemoteDataSource.getUserStatusList(user, skip, limit);
    }

    @Override
    public List<AVUser> getFollows(String userId, int skip, int limit) throws AVException{
        if(mLocalDataSource.getFollows(userId, skip,limit)==null){
            ((LocalDataSource)mLocalDataSource).follows = mAVRemoteDataSource.getFollows(userId, skip, limit);
        }
        return mLocalDataSource.getFollows(userId, skip,limit);
    }

    @Override
    public List<AVUser> getFollowing(String userId, int skip, int limit,boolean force) throws AVException{
        if(mLocalDataSource.getFollowing(userId, skip,limit,force)==null||force){
            ((LocalDataSource)mLocalDataSource).folling = mAVRemoteDataSource.getFollowing(userId, skip, limit,force);
        }
        return mLocalDataSource.getFollowing(userId, skip,limit,force);
    }

    @Override
    public List<Comment> getComments(String statusId, int skip, int limit) throws AVException {
        return mAVRemoteDataSource.getComments(statusId, skip, limit);
    }

    @Override
    public List<Subject> getSubjects(int skip, int limit) throws AVException {
        return mAVRemoteDataSource.getSubjects(skip,limit);
    }

    @Override
    public List<Subject> getAllSubjects(int skip, int limit) throws AVException {
        return mAVRemoteDataSource.getAllSubjects(skip, limit);
    }

    @Override
    public boolean getRelationship(User user, boolean isFollower) {
        return false;
    }

    @Override
    public Observable<String> addSendStatus(@NonNull String content,Bitmap bitmap) {
        return mAVRemoteDataSource.addSendStatus(content,bitmap);
    }

    @Override
    public void addComment(String statusId,@NonNull Comment comment) throws AVException{
        mAVRemoteDataSource.addComment(statusId,comment);
    }

    @Override
    public void addSubject(@NonNull Subject subject) throws AVException {
        mAVRemoteDataSource.addSubject(subject);
    }

    @Override
    public String addUploadFile(@NonNull Bitmap bitmap) throws AVException {
        return mAVRemoteDataSource.addUploadFile(bitmap);
    }

    @Override
    public void updateFollowAction(User user, boolean follow) {

    }

    @Override
    public void updateStatusLikes(Status status, List<String> likes) throws AVException {
        mAVRemoteDataSource.updateStatusLikes(status, likes);
    }

    @Override
    public void deleteStatus(Status status) throws AVException {
        mAVRemoteDataSource.deleteStatus(status);
    }

    @Override
    public void deleteComment(Comment comment) throws AVException{
        mAVRemoteDataSource.deleteComment(comment);
    }

    @Override
    public void deleteSubject(Subject subject) throws AVException {
        mAVRemoteDataSource.deleteSubject(subject);
    }

    @Override
    public void deleteFile(String url) {

    }

    @Override
    public void login(String username, String password) throws AVException {
        mAVRemoteDataSource.login(username,password);
    }

    @Override
    public void signUp(User user) {

    }

    @Override
    public boolean IMLogin(String username, String password) {
        return false;
    }

    @Override
    public boolean IMRegister(String username, String password) {
        return false;
    }
}
