package com.wen.hugo.followPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class FollowPageActivity extends AppCompatActivity {

  private FollowPagePresenter mFollowPagePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.followpage_activity);

    FollowPageFragment followPageFragment =
            (FollowPageFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (followPageFragment == null) {
      followPageFragment = FollowPageFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              followPageFragment, R.id.contentFrame);

      // Create the presenter
      mFollowPagePresenter = new FollowPagePresenter(
              DataRepository.getInstance(),
              followPageFragment,
              SchedulerProvider.getInstance());
    }
  }

  public static void goFollow(Context context,int type) {
    Intent intent = new Intent(context, FollowPageActivity.class);
    intent.putExtra(FollowPageFragment.TYPE, type);
    context.startActivity(intent);
  }
}