package com.wen.hugo.personPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.wen.hugo.ListView.BaseListView;
import com.wen.hugo.ListView.Status;
import com.wen.hugo.ListView.StatusNetAsyncTask;
import com.wen.hugo.ListView.StatusUtils;
import com.wen.hugo.R;
import com.wen.hugo.timeLine.StatusListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends Activity {
  public static final String USER_ID = "userId";
  public static final int CANCEL_FOLLOW = 0;
  public static final int FOLLOW = 1;


  @BindView(R.id.status_List)
  BaseListView<Status> statusList;

  @BindView(R.id.followAction)
  Button followActionBtn;

  @BindView(R.id.followStatus)
  TextView followStatusView;

  @BindView(R.id.followLayout)
  View followLayout;

  int followStatus;

  AVUser user;

  boolean myself;

  int actionType = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.status_person_layout);
    init();
    ButterKnife.bind(this);
    initList();
    statusList.onRefresh();
    refresh();
  }

  private void refresh() {
    new StatusNetAsyncTask(this) {
      @Override
      protected void doInBack() throws Exception {
        if (!myself) {
          followStatus = followStatus(user);
        }
      }

      @Override
      protected void onPost(Exception e) {
        if (StatusUtils.filterException(PersonActivity.this, e)) {
          if (myself) {
            followStatusView.setVisibility(View.GONE);
            followLayout.setVisibility(View.GONE);
            followActionBtn.setVisibility(View.GONE);
            return;
          }
          followStatusView.setVisibility(View.VISIBLE);
          followActionBtn.setVisibility(View.VISIBLE);

          int followStatusDescId = R.string.status_none_follow_desc;
          switch (followStatus) {
            case MUTUAL_FOLLOW:
              followStatusDescId = R.string.status_mutual_follow;
              break;
            case FOLLOWER:
              followStatusDescId = R.string.status_follower_desc;
              break;
            case FOLLOWING:
              followStatusDescId = R.string.status_following_desc;
              break;
            case NONE_FOLLOW:
              followStatusDescId = R.string.status_none_follow_desc;
              break;
          }
          String followStatusDesc = getString(followStatusDescId);
          followStatusView.setText(followStatusDesc);

          int followButtonResId;
          if (followStatus == MUTUAL_FOLLOW ||
              followStatus == FOLLOWING) {
            actionType = CANCEL_FOLLOW;
            followButtonResId = R.string.status_cancelFollow;
          } else {
            actionType = FOLLOW;
            followButtonResId = R.string.status_follow;
          }

          followActionBtn.setText(getString(followButtonResId));
        }
      }
    }.execute();
  }

  public static final int MUTUAL_FOLLOW = 0;//disable follow
  public static final int FOLLOWER = 1;  //can follow
  public static final int FOLLOWING = 2;  //disable follow
  public static final int NONE_FOLLOW = 3; //can follow

  private void initList() {
    statusList.init(new BaseListView.DataInterface<Status>() {
      public List<Status> getDatas(int skip, int limit, List<Status> currentDatas) throws Exception {
        return getUserStatusList(user, skip, limit);
      }
    }, new StatusListAdapter(this));
    statusList.setToastIfEmpty(false);
  }

  private void init() {
    Intent intent = getIntent();
    String userId = intent.getStringExtra(USER_ID);
    AVUser currentUser = AVUser.getCurrentUser();
    myself = userId.equals(currentUser.getObjectId());
    if(myself){
      user = currentUser;
    }
  }

  public static List<Status> getUserStatusList(AVUser user, int skip, int limit) throws AVException {
    AVStatusQuery q = AVStatus.statusQuery(user);
    q.include(DETAIL_ID);
    q.orderByDescending(CREATED_AT);
    q.setSkip(skip);
    q.setLimit(limit);
    List<AVStatus> avStatuses = q.find();
    List<Status> statuses = new ArrayList<Status>();
    for(int i = 0; i < avStatuses.size(); i++){
      Status status = new Status();
      status.setInnerStatus(avStatuses.get(i));
      status.setDetail(avStatuses.get(i).getAVObject(DETAIL_ID));
      statuses.add(status);
    }
    return statuses;
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  public static void go(Context context, AVUser item) {
    Intent intent = new Intent(context, PersonActivity.class);
    intent.putExtra(PersonActivity.USER_ID, item.getObjectId());
    context.startActivity(intent);
  }

  @OnClick(R.id.followAction)
  void followAction() {
    if (actionType != -1) {
      if (myself) {
        return;
      }
      boolean follow;
      if (actionType == FOLLOW) {
        follow = true;
      } else {
        follow = false;
      }
      followAction(user, follow, new FollowCallback() {

        @Override
        public void done(AVObject object, AVException e) {
          if (StatusUtils.filterException(PersonActivity.this, e)) {
            refresh();
          }
        }
      });
    }
  }

  public static void followAction(AVUser user, boolean follow, FollowCallback followCallback) {
    AVUser currentUser = AVUser.getCurrentUser();
    if (follow) {
      currentUser.followInBackground(user.getObjectId(), followCallback);
    } else {
      currentUser.unfollowInBackground(user.getObjectId(), followCallback);
    }
  }

  public static int followStatus(AVUser user) throws AVException {
    boolean isMyFollower = findFollowStatus(user, true);
    boolean isMyFollowing = findFollowStatus(user, false);
    if (isMyFollower && isMyFollowing) {
      return MUTUAL_FOLLOW;
    } else if (isMyFollower) {
      return FOLLOWER;
    } else if (isMyFollowing) {
      return FOLLOWING;
    } else {
      return NONE_FOLLOW;
    }
  }
  public static final String FOLLOWERss = "follower";
  public static final String FOLLOWEEss = "followee";
  public static final String DETAIL_ID = "detailId";
  public static final String CREATED_AT = "createdAt";

  public static boolean findFollowStatus(AVUser user, boolean askFollower) throws AVException {
    AVUser currentUser = AVUser.getCurrentUser();
    AVQuery<AVUser> q;
    if (askFollower) {
      q = AVUser.followerQuery(currentUser.getObjectId(), AVUser.class);
      q.whereEqualTo(FOLLOWERss, user);
    } else {
      q = AVUser.followeeQuery(currentUser.getObjectId(), AVUser.class);
      q.whereEqualTo(FOLLOWEEss, user);
    }
    q.setLimit(1);
    List<AVUser> avUsers = q.find();
    return avUsers.isEmpty() == false;
  }
}
