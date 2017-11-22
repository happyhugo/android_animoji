package com.wen.hugo.publishStatus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class PublishStatusActivity extends AppCompatActivity {

  private PublishStatusPresenter mPublishPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.publish_activity);

    PublishStatusFragment publishFragment =
            (PublishStatusFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (publishFragment == null) {
      publishFragment = PublishStatusFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              publishFragment, R.id.contentFrame);

      // Create the presenter
      mPublishPresenter = new PublishStatusPresenter(
              DataRepository.getInstance(),
              publishFragment,
              SchedulerProvider.getInstance());
    }
  }
}