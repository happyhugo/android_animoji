package com.wen.hugo.bean;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;

/**
 * Created by hugo on 11/21/17.
 */

public class Comment {
    public static final String FROM = "from";
    public static final String REPLAY = "replay";
    public static final String COMMENT = "Comment";
    public static final String CONTENT = "content";
    public static final String STATUS_ID = "statusId";

    private AVStatus innerStatus;
    private AVObject comment;
    private AVUser from;
    private AVUser replayTo;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AVUser getReplayTo() {
        return replayTo;
    }

    public void setReplayTo(AVUser replayTo) {
        this.replayTo = replayTo;
    }

    public AVUser getFrom() {
        return from;
    }

    public void setFrom(AVUser from) {
        this.from = from;
    }

    public AVObject getComment() {
        return comment;
    }

    public void setComment(AVObject comment) {
        this.comment = comment;
    }

    public AVStatus getInnerStatus() {
        return innerStatus;
    }

    public void setInnerStatus(AVStatus innerStatus) {
        this.innerStatus = innerStatus;
    }
}
