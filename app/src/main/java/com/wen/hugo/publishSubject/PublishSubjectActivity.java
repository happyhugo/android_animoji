package com.wen.hugo.publishSubject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class PublishSubjectActivity extends AppCompatActivity {

  private PublishSubjectPresenter mPublishPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.publishsubject_activity);

    PublishSubjectFragment publishFragment =
            (PublishSubjectFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (publishFragment == null) {
      publishFragment = PublishSubjectFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              publishFragment, R.id.contentFrame);

      // Create the presenter
      mPublishPresenter = new PublishSubjectPresenter(
              DataRepository.getInstance(),
              publishFragment,
              SchedulerProvider.getInstance());
    }
  }
}