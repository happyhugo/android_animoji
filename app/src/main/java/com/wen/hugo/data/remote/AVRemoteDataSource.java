package com.wen.hugo.data.remote;

import android.support.annotation.NonNull;

import com.wen.hugo.Bean.Comment;
import com.wen.hugo.Bean.Status;
import com.wen.hugo.Bean.User;
import com.wen.hugo.data.DataSource;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by hugo on 11/21/17.
 */

public class AVRemoteDataSource implements DataSource {

    private static AVRemoteDataSource INSTANCE;

    public static AVRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AVRemoteDataSource();
        }
        return INSTANCE;
    }

    private AVRemoteDataSource(){}

    @Override
    public Flowable<List<Status>> getNewStatus(int skip, int limit) {
        return null;
    }

    @Override
    public Flowable<List<Status>> getTimeline(User my, long maxId, int limit) {
        return null;
    }

    @Override
    public Flowable<List<Status>> getUserStatus(User user, long maxId, int limit) {
        return null;
    }

    @Override
    public Flowable<List<User>> getFollowers(User user, int skip, int limit) {
        return null;
    }

    @Override
    public Flowable<List<User>> getFollowings(User user, int skip, int limit) {
        return null;
    }

    @Override
    public Flowable<List<Comment>> getComments(Status status, int skip, int limit) {
        return null;
    }

    @Override
    public boolean getRelationship(User user, boolean isFollower) {
        return false;
    }

    @Override
    public void addSendStatus(@NonNull Status status) {

    }

    @Override
    public void addComment(@NonNull Comment comment) {

    }

    @Override
    public String addUploadFile(@NonNull String path) {
        return null;
    }

    @Override
    public void updateFollowAction(User user, boolean follow) {

    }

    @Override
    public void updateStatusLikes(Status status, List<String> likes) {

    }

    @Override
    public void deleteStatus(Status status) {

    }

    @Override
    public void deleteComment(Comment comment) {

    }

    @Override
    public void deleteFile(String url) {

    }

    @Override
    public User login(User user) {
        return null;
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
