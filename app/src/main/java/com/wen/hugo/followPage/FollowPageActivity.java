package com.wen.hugo.followPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

import static com.wen.hugo.followPage.FollowPageFragment.TYPE_FOLLOWER;
import static com.wen.hugo.followPage.FollowPageFragment.TYPE_FOLLOWING;

public class FollowPageActivity extends AppCompatActivity {

  private FollowPagePresenter mFollowPagePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.followpage_activity);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final ActionBar ab = getSupportActionBar();
    Intent intent = getIntent();
    int type = intent.getIntExtra(FollowPageFragment.TYPE, TYPE_FOLLOWER);
    if(type==TYPE_FOLLOWER) {
        ab.setTitle("我的粉丝");
    }else if(type==TYPE_FOLLOWING){
        ab.setTitle("我的关注");
    }
    ab.setDisplayHomeAsUpEnabled(true);

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