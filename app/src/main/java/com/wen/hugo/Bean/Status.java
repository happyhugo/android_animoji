package com.wen.hugo.bean;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.util.Date;


public class Status {
  private AVUser user;
  private AVObject detail;
  private AVObject status;
  private boolean updateStatus;
  private String message;
  private String img;
  private Date date;

  public AVObject getStatus() {
    return status;
  }

  public void setStatus(AVObject status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public AVUser getUser() {
    return user;
  }

  public void setUser(AVUser user) {
    this.user = user;
  }

  public boolean isUpdateStatus() {
    return updateStatus;
  }

  public void setUpdateStatus(boolean updateStatus) {
    this.updateStatus = updateStatus;
  }

  public AVObject getDetail() {
    return detail;
  }

  public void setDetail(AVObject detail) {
    this.detail = detail;
  }
}
