package com.wen.hugo.data.remote;

import android.support.annotation.NonNull;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wen.hugo.bean.Comment;
import com.wen.hugo.bean.Status;
import com.wen.hugo.bean.User;
import com.wen.hugo.data.DataSource;

import java.util.List;

import io.reactivex.Observable;

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
    public Observable<List<Status>> getNewStatus(int skip, int limit) {
        return null;
    }

    @Override
    public Observable<List<Status>> getTimeline(User my, long maxId, int limit) {
//        return Observable.just(my)
//                .flatMap(new Function<User, Observable<List<Status>>>() {
//                    @Override
//                    public Observable<List<Status>> apply(User users) throws Exception {
//                        AVUser user = (AVUser ) users;
//                        AVStatusQuery q = AVStatus.inboxQuery(user, AVStatus.INBOX_TYPE.TIMELINE.toString());
//                        q.include(App.DETAIL_ID);
//                        q.setLimit(limit);
//                        q.setMaxId(maxId);
//                        q.orderByDescending(App.CREATED_AT);
//                        List<AVStatus> avStatuses = q.find();
//                        List<Status> statuses = new ArrayList<Status>();
//                        System.out.println("bbbb:"+avStatuses.size());
//                        for(int i = 0; i < avStatuses.size(); i++){
//                            Status status = new Status();
//                            status.setInnerStatus(avStatuses.get(i));
//                            status.setDetail(avStatuses.get(i).getAVObject(App.DETAIL_ID));
//                            statuses.add(status);   //包括两张表的对象
//                        }
//                        return Observable.just(a);
//                    }
//                }).observeOn(Schedulers.io());
        return null;
    }

    @Override
    public Observable<List<Status>> getUserStatus(User user, long maxId, int limit) {
        return null;
    }

    @Override
    public Observable<List<User>> getFollowers(User user, int skip, int limit) {
        return null;
    }

    @Override
    public Observable<List<User>> getFollowings(User user, int skip, int limit) {
        return null;
    }

    @Override
    public Observable<List<Comment>> getComments(Status status, int skip, int limit) {
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
