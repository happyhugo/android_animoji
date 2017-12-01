package com.wen.hugo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.wen.hugo.R;
import com.wen.hugo.chatPage.ChatActivity;
import com.wen.hugo.data.DataRepository;
import com.wen.hugo.timeLine.TimeLineFragment;
import com.wen.hugo.timeLine.TimeLinePresenter;
import com.wen.hugo.userPage.UserPageFragment;
import com.wen.hugo.userPage.UserPagePresenter;
import com.wen.hugo.util.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * TODOx
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(savedInstanceState);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TimeLineFragment)adapter.getItem(0)).send();
            }
        });

        bottomNavigationBar.clearAll();
        bottomNavigationBar.setFab(fab);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home").setActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "Books").setActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "Music").setActiveColorResource(R.color.white))
//                .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "Movies & TV").setActiveColorResource(R.color.brown))
                .setFirstSelectedPosition(0)
                .initialise();
    }


    private void setupViewPager(Bundle savedInstanceState) {
        adapter = new Adapter(getSupportFragmentManager());

        if(savedInstanceState==null) {
            TimeLineFragment timeLineFragment = TimeLineFragment.newInstance(false);
            new TimeLinePresenter(DataRepository.getInstance(), timeLineFragment, SchedulerProvider.getInstance());
            adapter.addFragment(timeLineFragment);

            UserPageFragment userPageFragment = UserPageFragment.newInstance();
            new UserPagePresenter(DataRepository.getInstance(), userPageFragment, SchedulerProvider.getInstance());
            adapter.addFragment(userPageFragment);

            EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
            adapter.addFragment(conversationListFragment);
            conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

                @Override
                public void onListItemClicked(EMConversation conversation) {
                    startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
                }
            });
        }else{
            adapter.addFragment(getSupportFragmentManager().findFragmentByTag(Adapter.makeFragmentName(viewPager.getId(),0)));
            adapter.addFragment(getSupportFragmentManager().findFragmentByTag(Adapter.makeFragmentName(viewPager.getId(),1)));
            adapter.addFragment(getSupportFragmentManager().findFragmentByTag(Adapter.makeFragmentName(viewPager.getId(),2)));
        }

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);

    }


    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public static String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}

