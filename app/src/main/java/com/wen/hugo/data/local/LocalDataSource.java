package com.wen.hugo.data.local;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.bean.Status;
import com.wen.hugo.bean.User;
import com.wen.hugo.data.DataSource;

import java.util.List;

import io.agora.openvcall.model.Subject;
import io.reactivex.Observable;

/**
 * Created by hugo on 11/21/17.
 */

public class LocalDataSource implements DataSource {
    public List<AVUser> follows;
    public List<AVUser> folling;

    @Override
    public List<Status> getNewStatus(int skip, int limit) {
        return null;
    }

    @Override
    public List<Status> getTimeline(int skip, int limit) throws AVException {
        return null;
    }

    @Override
    public List<Status> getUserStatusList(AVUser user, int skip, int limit) throws AVException {
        return null;
    }

    @Override
    public List<AVUser> getFollows(String userId, int skip, int limit) throws AVException {
        return follows;
    }

    @Override
    public List<AVUser> getFollowing(String userId, int skip, int limit,boolean force) throws AVException {
        return folling;
    }

    @Override
    public List<Comment> getComments(String statusId, int skip, int limit) throws AVException {
        return null;
    }

    @Override
    public List<Subject> getSubjects(int skip, int limit) throws AVException {
        return null;
    }

    @Override
    public List<Subject> getAllSubjects(int skip, int limit) throws AVException {
        return null;
    }

    @Override
    public boolean getRelationship(User user, boolean isFollower) {
        return false;
    }

    @Override
    public Observable<String> addSendStatus(@NonNull final String content,final Bitmap bitmap) {
           return null;
    }

    @Override
    public void addComment(String statusId,@NonNull Comment comment) {

    }

    @Override
    public void addSubject(@NonNull Subject subject) throws AVException {

    }

    @Override
    public String addUploadFile(@NonNull Bitmap bitmap) {
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
    public void deleteSubject(Subject subject) throws AVException {

    }

    @Override
    public void deleteFile(String url) {

    }

    @Override
    public void login(String username, String password) throws AVException {

    }

    @Override
    public void register(String username, String password) throws AVException {

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
