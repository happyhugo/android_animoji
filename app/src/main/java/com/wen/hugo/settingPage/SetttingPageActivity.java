package com.wen.hugo.settingPage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wen.hugo.R;


public class SetttingPageActivity extends Activity {
  Button button;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settingpage_layout);
    button = (Button) findViewById(R.id.loginout);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
      }
    });
  }
}
