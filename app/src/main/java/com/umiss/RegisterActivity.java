package com.umiss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import models.Monitor;
import network.UMISSRest;

public class RegisterActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private EditText passwordConfirmation;
    private EditText chairToken;

    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewItems();
    }

    private void findViewItems(){
        user = (EditText) findViewById(R.id.register_et_user);
        password = (EditText) findViewById(R.id.register_et_password);
        passwordConfirmation = (EditText) findViewById(R.id.register_et_confirm_password);
        chairToken = (EditText) findViewById(R.id.register_et_chair_token);
        register = (Button) findViewById(R.id.register_btn_register);
        register.setOnClickListener(registerOnClickListener);
    }

    private void sendInformation(String user, String password, String chairToken){

        String token = FirebaseInstanceId.getInstance().getToken().toString();
        Monitor monitor = new Monitor(user, password, chairToken, token);
        UMISSRest.register(UMISSRest.getAbsoluteURL("api/monitors"), getApplicationContext(), monitor, new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if ( result != null ) {
                    Toast.makeText(getApplicationContext(), "Registrou!", Toast.LENGTH_LONG).show();
                    startLoginActivity();
                }else {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("Register2", e.toString());
                }
            }
        });
    }

    private void startLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /* on clicks */

    private View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userName = user.getText().toString();
            String passwordString = password.getText().toString();
            String chairTokenString = chairToken.getText().toString();
            sendInformation(userName, passwordString, chairTokenString);
            Log.d("Register", "Clicked!");
        }
    };


}
