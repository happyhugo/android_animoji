package com.wen.hugo.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class LoginActivity extends AppCompatActivity {

  private LoginPresenter mLoginPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
           /*set it to be no title*/
    requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.login_activity);

    LoginFragment loginFragment =
            (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

    if (loginFragment == null) {
      loginFragment = LoginFragment.newInstance();

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              loginFragment, R.id.contentFrame);

      // Create the presenter
      mLoginPresenter = new LoginPresenter(
              DataRepository.getInstance(),
              loginFragment,
              SchedulerProvider.getInstance());
    }
  }
}