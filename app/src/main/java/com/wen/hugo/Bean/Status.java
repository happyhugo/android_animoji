package com.wen.hugo.bean;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVStatus;


public class Status {
  private AVStatus innerStatus;
  private AVObject detail;
  private boolean updateStatus;

  public boolean isUpdateStatus() {
    return updateStatus;
  }

  public void setUpdateStatus(boolean updateStatus) {
    this.updateStatus = updateStatus;
  }

  public AVStatus getInnerStatus() {
    return innerStatus;
  }

  public void setInnerStatus(AVStatus innerStatus) {
    this.innerStatus = innerStatus;
  }

  public AVObject getDetail() {
    return detail;
  }

  public void setDetail(AVObject detail) {
    this.detail = detail;
  }
}
