package com.umiss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button button;

    private HeartBeatsFragment heartBeatsFragment;
    private TemperatureFragment temperatureFragment;
    private GalvanicFragment galvanicFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLogged();
        findViewItems();
        instantiateFragments();
    }

    private void instantiateFragments() {
        heartBeatsFragment = new HeartBeatsFragment();
        temperatureFragment = new TemperatureFragment();
        galvanicFragment = new GalvanicFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    private void findViewItems(){
        tabLayout = (TabLayout) findViewById(R.id.main_tab);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        button = (Button) findViewById(R.id.toolbar_button);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LoginActivity.IS_LOGGED, "notlogged");
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void isLogged(){

        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);

        String isLogged = "";

        try {
            isLogged = sharedPreferences.getString(LoginActivity.IS_LOGGED, "notlogged");
        }catch(ClassCastException c){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }

        try{
            Log.d("tokenAndroid", FirebaseInstanceId.getInstance().getToken().toString());
        }catch (NullPointerException e){
            Log.d("tokenAndroid", "Firebase instance null");
            isLogged = "";
        }


        if ( !isLogged.equals("logged") ){

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class CustomAdapter extends FragmentPagerAdapter {

        private String[] fragments = {"Batimentos", "Galvanica", "Temperatura"};

        public CustomAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 0:
                    return heartBeatsFragment;
                case 1:
                    return galvanicFragment;
                case 2:
                    return temperatureFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return fragments[position];
        }
    }
}
