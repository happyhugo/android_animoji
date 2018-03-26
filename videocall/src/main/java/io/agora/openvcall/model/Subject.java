package io.agora.openvcall.model;

import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 12/8/17.
 */

public class Subject {
    public static final String FROM = "from";
    public static final String USERNAME = "username";
    public static final String TITLE = "title";
    public static final String SUBJECT = "Subject";
    public static final String CONTENT = "content";

    private String username;
    private String title;
    private List<String> content;
    private AVUser from;
    private String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public AVUser getFrom() {
        return from;
    }

    public void setFrom(AVUser from) {
        this.from = from;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private static List<Subject> lv  = new ArrayList();

    public static boolean addSubject(Subject subject){
        boolean add = true;
        for(Subject getData: lv){
            if(getData.getObjectId().equals(subject.getObjectId())){
                add = false;
                break;
            }
        }
        if(add){
            lv.add(subject);
            return true;
        }else{
            return false;
        }
    }



    public static List<Subject> getSubjects(){
        return lv;
    }

    public static boolean isSubjectNull(){
        return lv.size()==0;
    }
}
