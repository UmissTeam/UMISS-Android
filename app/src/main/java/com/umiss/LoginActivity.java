package com.umiss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static final String IS_LOGGED = "isLogged";

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

        EditText user = (EditText) findViewById(R.id.editText_user);
        EditText password = ((EditText) findViewById(R.id.editText_password));
        String token = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        JSONObject jsonObject = authenticate(user.getText().toString(), password.getText().toString(), token);

        if ( jsonObject != null ){

            pushLoginCredentials(sharedPreferences, jsonObject);
            startMainActivity();
        }else{

            loginHasFailed(user, password);
        }
    }

    private JSONObject authenticate(String user, String password, String token) throws JSONException {

        JSONObject jsonObject = new JSONObject();

        //TODO: Connect with server
        if ( user.equals("hard") && password.equals("coded") ){

            jsonObject.put("authenticationToken", "authenticationToken");
            return jsonObject;
        }

        return null;
    }

    private void pushLoginCredentials(SharedPreferences sharedPreferences, JSONObject jsonObject) throws JSONException {

        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean(IS_LOGGED, true);
        //TODO: in case this token is necessary
        prefEditor.putString("authenticationToken", jsonObject.getString("authenticationToken"));
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
