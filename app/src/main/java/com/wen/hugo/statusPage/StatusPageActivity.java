package com.wen.hugo.statusPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVUser;
import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.userPage.UserPageActivity;
import com.wen.hugo.userPage.UserPageFragment;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class StatusPageActivity extends AppCompatActivity {

  private StatusPagePresenter mStatusPagePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.statuspage_activity);

    StatusPageFragment statusPageFragment =
            (StatusPageFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (statusPageFragment == null) {
      statusPageFragment = StatusPageFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              statusPageFragment, R.id.contentFrame);

      // Create the presenter
      mStatusPagePresenter = new StatusPagePresenter(
              DataRepository.getInstance(),
              statusPageFragment,
              SchedulerProvider.getInstance());
    }
  }

  public static void go(Context context, String statusId) {
    Intent intent = new Intent(context, StatusPageActivity.class);
    intent.putExtra(StatusPageFragment.STATUS_ID, statusId);
    context.startActivity(intent);
  }
}