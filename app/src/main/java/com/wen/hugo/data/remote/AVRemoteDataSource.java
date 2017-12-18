package com.wen.hugo.data.remote;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.bean.Status;
import com.wen.hugo.bean.Subject;
import com.wen.hugo.bean.User;
import com.wen.hugo.data.DataSource;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by hugo on 11/21/17.
 */

public class AVRemoteDataSource implements DataSource {

    public static final String LIKES = "likes";
    public static final String STATUS_DETAIL = "StatusDetail";
    public static final String DETAIL_ID = "detailId";
    public static final String CREATED_AT = "createdAt";

    public static final String FOLLOWER = "follower";
    public static final String FOLLOWEE = "followee";

    private static AVRemoteDataSource INSTANCE;

    public static AVRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new AVRemoteDataSource();
        }
        return INSTANCE;
    }

    private AVRemoteDataSource(){}

    @Override
    public List<Status> getNewStatus(int skip, int limit) throws AVException {
        AVQuery<AVObject> q = new AVQuery<>("_Status");
        q.include("source");
        q.include(DETAIL_ID);
        q.setLimit(limit);
        q.setSkip(skip);
        q.orderByDescending(CREATED_AT);
        List<AVObject> avStatuses = q.find();
        List<Status> statuses = new ArrayList<>();
        for(int i = 0; i < avStatuses.size(); i++){
            Status status = new Status();
            status.setStatus(avStatuses.get(i));
            status.setDetail(avStatuses.get(i).getAVObject(DETAIL_ID));
            if(avStatuses.get(i) instanceof AVStatus){
                status.setUser(((AVStatus)avStatuses.get(i)).getSource() );
                status.setMessage(((AVStatus)avStatuses.get(i)).getMessage());
                status.setImg(((AVStatus)avStatuses.get(i)).getImageUrl());
                status.setDate(((AVStatus)avStatuses.get(i)).getCreatedAt());
            }else{
                status.setUser((AVUser) avStatuses.get(i).getAVObject("source"));
                status.setDate((Date) avStatuses.get(i).get("createdAt"));
                status.setImg((String)avStatuses.get(i).get("image"));
                status.setMessage((String)avStatuses.get(i).get("message"));
            }
            statuses.add(status);   //包括两张表的对象
        }
        return statuses;
    }

    @Override
    public List<Status> getTimeline(int skip, int limit) throws AVException {
        AVUser user = AVUser.getCurrentUser();
        AVStatusQuery q = AVStatus.inboxQuery(user, AVStatus.INBOX_TYPE.TIMELINE.toString());
        q.include(DETAIL_ID);
        q.setLimit(limit);
        q.setSkip(skip);
        q.orderByDescending(CREATED_AT);
        List<AVStatus> avStatuses = q.find();
        List<Status> statuses = new ArrayList<>();
        for(int i = 0; i < avStatuses.size(); i++){
            Status status = new Status();
            status.setStatus(avStatuses.get(i));
            status.setUser(avStatuses.get(i).getSource());
            status.setDetail(avStatuses.get(i).getAVObject(DETAIL_ID));
            status.setMessage(avStatuses.get(i).getMessage());
            status.setImg(avStatuses.get(i).getImageUrl());
            status.setDate(avStatuses.get(i).getCreatedAt());
            statuses.add(status);   //包括两张表的对象
        }
        return statuses;
    }

    @Override
    public List<Status> getUserStatusList(AVUser user, int skip, int limit) throws AVException {
        AVStatusQuery q = AVStatus.statusQuery(user);
        q.include(DETAIL_ID);
        q.orderByDescending(CREATED_AT);
        q.setSkip(skip);
        q.setLimit(limit);
        List<AVStatus> avStatuses = q.find();
        List<Status> statuses = new ArrayList<Status>();
        for(int i = 0; i < avStatuses.size(); i++){
            Status status = new Status();
            status.setStatus(avStatuses.get(i));
            status.setUser(avStatuses.get(i).getSource());
            status.setDetail(avStatuses.get(i).getAVObject(DETAIL_ID));
            status.setMessage(avStatuses.get(i).getMessage());
            status.setImg(avStatuses.get(i).getImageUrl());
            status.setDate(avStatuses.get(i).getCreatedAt());
            statuses.add(status);
        }
        return statuses;
    }

    @Override
    public List<AVUser> getFollows(String userId, int skip, int limit) throws AVException {
        AVQuery<AVUser> q = AVUser.followerQuery(userId,AVUser.class);
        q.skip(skip);
        q.limit(limit);
        q.include(FOLLOWER);
        return q.find();
    }

    @Override
    public List<AVUser> getFollowing(String userId, int skip, int limit,boolean force) throws AVException {
        AVQuery<AVUser> q = AVUser.followeeQuery(userId,AVUser.class);
        q.skip(skip);
        q.limit(limit);
        q.include(FOLLOWEE);
        return q.find();
    }

    @Override
    public List<Comment> getComments(String statusId, int skip, int limit) throws AVException {
        AVQuery<AVObject> query = new AVQuery<>(Comment.COMMENT);
        query.whereEqualTo(Comment.STATUS_ID, statusId);
        query.setSkip(skip);
        query.setLimit(limit);
        query.orderByAscending("createdAt");
        query.include(Comment.FROM);
//      query.include(Comment.REPLAY);
        List<AVObject> avComments = query.find();
        List<Comment> comments = new ArrayList<>();
        for(int i=0;i<avComments.size();i++){
            Comment comment = new Comment();
            comment.setComment(avComments.get(i));
            comment.setFrom(avComments.get(i).getAVUser(Comment.FROM));
            comment.setReplayTo(avComments.get(i).getAVUser(Comment.REPLAY));
            comment.setNumber(skip+i+1);
            comments.add(comment);
        }
        return comments;
    }

    @Override
    public List<Subject> getSubjects(int skip, int limit) throws AVException {
        AVQuery<AVObject> query = new AVQuery<>(Subject.SUBJECT);
        query.whereEqualTo(Subject.FROM, AVUser.getCurrentUser());
        query.setSkip(skip);
        query.setLimit(limit);
        query.orderByAscending("createdAt");
        List<AVObject> avSubjects = query.find();
        List<Subject> subjects = new ArrayList<>();
        for(int i=0;i<avSubjects.size();i++){
            Subject subject = new Subject();
            subject.setUsername((String)avSubjects.get(i).get(Subject.USERNAME));
            subject.setTitle((String)avSubjects.get(i).get(Subject.TITLE));
            subject.setContent(avSubjects.get(i).getList(Subject.CONTENT));
            subject.setFrom(AVUser.getCurrentUser());
            subject.setObjectId(avSubjects.get(i).getObjectId());
            subjects.add(subject);
        }
        return subjects;
    }

    @Override
    public List<Subject> getAllSubjects(int skip, int limit) throws AVException {
        AVQuery<AVObject> query = new AVQuery<>(Subject.SUBJECT);
        query.setSkip(skip);
        query.setLimit(limit);
        query.orderByAscending(CREATED_AT);
        List<AVObject> avSubjects = query.find();
        List<Subject> subjects = new ArrayList<>();
        for(int i=0;i<avSubjects.size();i++){
            Subject subject = new Subject();
            subject.setUsername((String)avSubjects.get(i).get(Subject.USERNAME));
            subject.setTitle((String)avSubjects.get(i).get(Subject.TITLE));
            subject.setContent(avSubjects.get(i).getList(Subject.CONTENT));
            subject.setFrom(AVUser.getCurrentUser());
            subject.setObjectId(avSubjects.get(i).getObjectId());
            subjects.add(subject);
        }
        return subjects;
    }

    @Override
    public boolean getRelationship(User user, boolean isFollower) {
        return false;
    }

    @Override
    public Observable<String> addSendStatus(@NonNull final String content,final Bitmap bitmap) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                String url = "";
                if(bitmap!=null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    byte[] bs = out.toByteArray();
                    AVUser user = AVUser.getCurrentUser();
                    String name = user.getUsername() + "_" + System.currentTimeMillis();
                    final AVFile file = new AVFile(name, bs);
                    file.save();
                    url = file.getUrl();
                }
                AVObject statusDetail = new AVObject(STATUS_DETAIL);
                statusDetail.save();
                AVStatus status = new AVStatus();
                status.setMessage(content);
                status.setImageUrl(url);
                status.put(DETAIL_ID, statusDetail);
                AVStatus.sendStatusToFollowersInBackgroud(status, new SaveCallback() {
                    @Override
                    public void done(AVException exception) {
                         if(exception!=null){
                             //这个时候其实应该将图片删除掉
                             e.onNext(exception.getMessage());
                         }else{
                             e.onNext("");
                         }
                    }
                });
            }
        });
    }

    @Override
    public void addComment(String statusId,@NonNull Comment comment) throws AVException {
        AVObject comments = new AVObject(Comment.COMMENT);
        comments.put(Comment.FROM,comment.getFrom());
        if(comment.getReplayTo()!=null) {
            comments.put(Comment.REPLAY, comment.getReplayTo());
        }
        comments.put(Comment.CONTENT,comment.getContent());
        comments.put(Comment.STATUS_ID,statusId);
        comment.setComment(comments);
        comments.save();
    }

    @Override
    public void addSubject(@NonNull Subject subject) throws AVException {
        AVObject subjects = new AVObject(Subject.SUBJECT);
        subjects.put(Subject.CONTENT,subject.getContent());
        subjects.put(Subject.TITLE,subject.getTitle());
        subjects.put(Subject.USERNAME,subject.getUsername());
        subjects.put(Subject.FROM,AVUser.getCurrentUser());
        subjects.save();
    }



    @Override
    public String addUploadFile(@NonNull Bitmap bitmap) throws AVException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        byte[] bs = out.toByteArray();
        AVUser user = AVUser.getCurrentUser();
        String name = user.getUsername() + "_" + System.currentTimeMillis();
        final AVFile file = new AVFile(name, bs);
        file.save();
        return file.getUrl();
    }

    @Override
    public void updateFollowAction(User user, boolean follow) {

    }

    @Override
    public void updateStatusLikes(Status status, List<String> likes) throws AVException {
        AVObject detail = status.getDetail();
        detail.put(LIKES, likes);
        detail.save();
    }

    @Override
    public void deleteStatus(Status status) throws AVException {
        status.getStatus().delete();
        status.getDetail().delete();
    }

    @Override
    public void deleteComment(Comment comment)throws AVException{
        AVObject comments = comment.getComment();
        comments.delete();
    }

    @Override
    public void deleteSubject(Subject subject) throws AVException {
        AVObject avObject = AVObject.createWithoutData(Subject.SUBJECT,subject.getObjectId());
        avObject.delete();
    }

    @Override
    public void deleteFile(String url) {

    }

    @Override
    public void login(String username,String password) throws AVException {
        AVUser.logIn(username,password);
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
