package com.wen.hugo.register;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class RegisterActivity extends AppCompatActivity {

  private RegisterPresenter mLoginPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
           /*set it to be no title*/
    requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.register_activity);

    RegisterFragment loginFragment =
            (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (loginFragment == null) {
      loginFragment = RegisterFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              loginFragment, R.id.contentFrame);

      // Create the presenter
      mLoginPresenter = new RegisterPresenter(
              DataRepository.getInstance(),
              loginFragment,
              SchedulerProvider.getInstance());
    }
  }
}