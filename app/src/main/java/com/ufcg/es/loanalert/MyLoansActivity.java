package com.ufcg.es.loanalert;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MyLoansActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.floatingActionButton) FloatingActionButton floatingActionButton;

    private boolean boundToAlarmService;
    private ServiceConnection alarmServiceConnection = new AlarmServiceConnection();

    private class AlarmServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, final IBinder service) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    AlarmService.AlarmServiceBinder binder =
                            (AlarmService.AlarmServiceBinder) service;
                    boundToAlarmService = true;
                    AlarmService alarmService = binder.getService();
                    alarmService.startAlarmService();
                }

            }).start();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            boundToAlarmService = false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loans);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return new MyLoansFragment();
            }

            @Override
            public int getCount() {
                return 1;
            }

        };
        viewPager.setAdapter(adapter);
        floatingActionButton.setImageDrawable(new IconDrawable(this, "md-alarm-add")
            .actionBarSize().colorRes(android.R.color.white));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoanEntryActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent alarmService = new Intent(getApplicationContext(), AlarmService.class);
        bindService(alarmService, alarmServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        if (boundToAlarmService && alarmServiceConnection != null) {
            unbindService(alarmServiceConnection);
            boundToAlarmService = false;
        }
        super.onStop();
    }

}
