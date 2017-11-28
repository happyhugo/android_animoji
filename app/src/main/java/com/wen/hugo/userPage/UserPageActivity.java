package com.wen.hugo.userPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class UserPageActivity extends AppCompatActivity {

  private UserPagePresenter mUserPagePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.timeline_activity);

    UserPageFragment userPageFragment =
            (UserPageFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (userPageFragment == null) {
      userPageFragment = UserPageFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              userPageFragment, R.id.contentFrame);

      // Create the presenter
      mUserPagePresenter = new UserPagePresenter(
              DataRepository.getInstance(),
              userPageFragment,
              SchedulerProvider.getInstance());
    }
  }


  public static void go(Context context, AVUser item) {
    Intent intent = new Intent(context, UserPageActivity.class);
    intent.putExtra(UserPageFragment.USER, item);
    context.startActivity(intent);
  }
}