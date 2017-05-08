package com.umiss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import network.UMISSRest;

public class MainActivity extends AppCompatActivity {

    public static boolean isOnBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLogged();

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

                UMISSRest.post(UMISSRest.getAbsoluteURL("api/heart_beats"), jsonObject, getApplicationContext(), token,
                        new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println(result.toString());
                    }
                });
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

    private void isLogged(){

        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);

        if ( !sharedPreferences.getBoolean(LoginActivity.IS_LOGGED, false) ){

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
