package com.wen.hugo.timeLine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class TimeLineActivity extends AppCompatActivity {

  private TimeLinePresenter mTimeLinePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.timeline_activity);

    TimeLineFragment timeLineFragment =
            (TimeLineFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (timeLineFragment == null) {
      timeLineFragment = TimeLineFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              timeLineFragment, R.id.contentFrame);

      // Create the presenter
      mTimeLinePresenter = new TimeLinePresenter(
              DataRepository.getInstance(),
              timeLineFragment,
              SchedulerProvider.getInstance());
    }
  }
}