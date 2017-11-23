package com.wen.hugo.timeLine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.wen.hugo.ListView.Status;
import com.wen.hugo.R;
import com.wen.hugo.ListView.BaseListView;
import com.wen.hugo.publishStatus.PublishStatusActivity;
import com.wen.hugo.ListView.StatusNetAsyncTask;
import com.wen.hugo.ListView.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by lzw on 15/1/2.
 */
public class StatusListActivity extends Activity {
  private static final int SEND_REQUEST = 2;
  @BindView(R.id.status_List)
  BaseListView<Status> statusList;

  protected void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
  }

  protected boolean filterException(Exception e) {
    if (e != null) {
      toast(e.getMessage());
      return false;
    } else {
      return true;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.status_list_layout);
    ButterKnife.bind(this);
    initList();
    statusList.setToastIfEmpty(false);
    statusList.onRefresh();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    if (item.getItemId() == R.id.logout) {
      AVUser.logOut();
      finish();
    }
    return super.onMenuItemSelected(featureId, item);
  }

  private void initList() {
    statusList.init(new BaseListView.DataInterface<Status>() {
      @Override
      public List<Status> getDatas(int skip, int limit, List<Status> currentDatas) throws Exception {
        long maxId;
        maxId = getMaxId(skip, currentDatas);
        if (maxId == 0) {
          return new ArrayList<>();
        } else {
          return getStatusDatas(maxId, limit);
        }
      }

      @Override
      public void onItemLongPressed(final Status item) {
        AVStatus innerStatus = item.getInnerStatus();
        AVUser source = innerStatus.getSource();
        if (source.getObjectId().equals(AVUser.getCurrentUser().getObjectId())) {
          AlertDialog.Builder builder = new AlertDialog.Builder(StatusListActivity.this);
          builder.setMessage("要删除这条状态么?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              new StatusNetAsyncTask(StatusListActivity.this) {
                @Override
                protected void doInBack() throws Exception {
                  deleteStatus(item);
                }

                @Override
                protected void onPost(Exception e) {
                  if (e != null) {
                    StatusUtils.toast(StatusListActivity.this, e.getMessage());
                  } else {
                    statusList.onRefresh();
                  }
                }
              }.execute();
            }
          }).setNegativeButton("取消", null);
          builder.show();
        }
      }
    }, new StatusListAdapter(StatusListActivity.this));
  }

  public static long getMaxId(int skip, List<Status> currentDatas) {
    long maxId;
    if (skip == 0) {
      maxId = Long.MAX_VALUE;
    } else {
      AVStatus lastStatus = currentDatas.get(currentDatas.size() - 1).getInnerStatus();
      maxId = lastStatus.getMessageId() - 1;
    }
    return maxId;
  }

  @OnClick(R.id.send)
  void goSend() {
    Intent intent = new Intent(StatusListActivity.this, PublishStatusActivity.class);
    startActivityForResult(intent, SEND_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == SEND_REQUEST) {
        statusList.onRefresh();
      }
    }
  }

  public static final String LIKES = "likes";
  public static final String STATUS_DETAIL = "StatusDetail";
  public static final String DETAIL_ID = "detailId";
  public static final String CREATED_AT = "createdAt";
  public static final String FOLLOWER = "follower";
  public static final String FOLLOWEE = "followee";

  public static List<Status> getStatusDatas(long maxId, int limit) throws AVException {
    AVUser user = AVUser.getCurrentUser();
    AVStatusQuery q = AVStatus.inboxQuery(user, AVStatus.INBOX_TYPE.TIMELINE.toString());
    q.include(DETAIL_ID);
    q.setLimit(limit);
    q.setMaxId(maxId);
    q.orderByDescending(CREATED_AT);
    List<AVStatus> avStatuses = q.find();
    List<Status> statuses = new ArrayList<Status>();
    for(int i = 0; i < avStatuses.size(); i++){
      Status status = new Status();
      status.setInnerStatus(avStatuses.get(i));
      status.setDetail(avStatuses.get(i).getAVObject(DETAIL_ID));
      statuses.add(status);   //包括两张表的对象
    }
    return statuses;
  }

  public static void deleteStatus(Status status) throws AVException {
    AVStatus innerStatus = status.getInnerStatus();
    innerStatus.delete();
    AVObject detail = status.getDetail();
    detail.delete();
  }
}
