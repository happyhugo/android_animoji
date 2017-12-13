package com.wen.hugo.publishStatus;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.wen.hugo.R;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.util.ActivityUtils;
import com.wen.hugo.util.schedulers.SchedulerProvider;

public class PublishStatusActivity extends AppCompatActivity {

    private PublishStatusPresenter mPublishPresenter;
    private PublishStatusFragment publishFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle("记录中");
//    ab.setHomeAsUpIndicator(R.drawable.icon_issue_close);
        ab.setDisplayHomeAsUpEnabled(true);


        publishFragment =
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.publish, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_compose:
                publishFragment.send();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}