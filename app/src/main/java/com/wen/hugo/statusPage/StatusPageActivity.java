package com.wen.hugo.statusPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wen.hugo.R;
import com.wen.hugo.bean.MessageEvent;
import com.wen.hugo.bean.Status;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

import org.greenrobot.eventbus.EventBus;

public class StatusPageActivity extends AppCompatActivity {

  private StatusPagePresenter mStatusPagePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.statuspage_activity);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final ActionBar ab = getSupportActionBar();
    ab.setTitle("帖子");
    ab.setDisplayHomeAsUpEnabled(true);

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

  public static void go(Context context, Status status) {
    EventBus.getDefault().postSticky(new MessageEvent(status));
    Intent intent = new Intent(context, StatusPageActivity.class);
    intent.putExtra(StatusPageFragment.STATUS_ID, status.getStatus().getObjectId());
    context.startActivity(intent);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}