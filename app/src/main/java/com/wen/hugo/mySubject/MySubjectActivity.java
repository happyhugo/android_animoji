package com.wen.hugo.mySubject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class MySubjectActivity extends AppCompatActivity {

  private MySubjectPresenter mMySubjectPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mysubject_activity);

    MySubjectFragment mySubjectFragment =
            (MySubjectFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (mySubjectFragment == null) {
      mySubjectFragment = MySubjectFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              mySubjectFragment, R.id.contentFrame);

      // Create the presenter
      mMySubjectPresenter = new MySubjectPresenter(
              DataRepository.getInstance(),
              mySubjectFragment,
              SchedulerProvider.getInstance());
    }
  }

  public static void go(Context context) {
    Intent intent = new Intent(context, MySubjectActivity.class);
    context.startActivity(intent);
  }
}