package com.umiss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import org.json.JSONException;

import network.UMISSRest;

public class LoginActivity extends AppCompatActivity {

    public static final String IS_LOGGED = "isLogged";
    private String LOGIN_REQUEST = "users/";

    private EditText userEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void login(View view) throws JSONException {

        userEditText = (EditText) findViewById(R.id.editText_user);
        passwordEditText = ((EditText) findViewById(R.id.editText_password));
        String token = FirebaseInstanceId.getInstance().getToken();

        authenticate(userEditText.getText().toString(), passwordEditText.getText().toString(), token);
    }

    private void authenticate(String user,String password, String token) {

        final JsonObject jsonObject = getJson(user, password, token);

        UMISSRest.post(UMISSRest.getAbsoluteURL(LOGIN_REQUEST), jsonObject, getApplicationContext(), new FutureCallback<JsonObject> (){
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if ( result.has("url") ) {
                    SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                    pushLoginCredentials(sharedPreferences, jsonObject);
                    startMainActivity();
                }else{

                    loginHasFailed(userEditText, passwordEditText);
                }
            }
        });
    }

    private JsonObject getJson(String user, String password, String token){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", user);
        jsonObject.addProperty("password", password);

        return jsonObject;
    }

    private void pushLoginCredentials(SharedPreferences sharedPreferences, JsonObject jsonObject) {

        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean(IS_LOGGED, true);
        //TODO: in case this token is necessary
        prefEditor.putString("authenticationToken", "token");
        prefEditor.commit();
    }

    private void loginHasFailed(EditText user, EditText password){

        Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
        user.setText("");
        password.setText("");
        user.requestFocus();
    }

    private void startMainActivity(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
