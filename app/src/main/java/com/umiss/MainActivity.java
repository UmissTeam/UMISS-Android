package com.umiss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import network.UMISSRest;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLogged();

        findViewItems();

        Button button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(FirebaseInstanceId.getInstance().getToken().toString());
                Log.d("token", FirebaseInstanceId.getInstance().getToken().toString());

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("beats", 123);

                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                String token = sharedPreferences.getString(LoginActivity.TOKEN, "");

                System.out.println("token "+token);

//                UMISSRest.sendAndroidToken("http://10.0.2.2:8000/api/monitors/17",
//                        getApplicationContext(),
//                        token, FirebaseInstanceId.getInstance().getToken().toString(),
//                        "cadeiratoken",new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        if ( result != null )
//                            System.out.println("result = "+result.toString());
//                        else
//                            System.out.println("e = " + e.toString());
//                    }
//                });
            }
        });
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

        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager()));


        viewPager.beginFakeDrag();

        tabLayout.setupWithViewPager(viewPager);
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
                    return new F1();
                case 1:
                    return new F1();
                case 2:
                    return new F1();
                case 3:
                    return new F1();
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
