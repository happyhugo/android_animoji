package com.wen.hugo.bean;

import com.avos.avoscloud.AVUser;

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
}
