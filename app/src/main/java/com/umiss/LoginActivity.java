package com.umiss;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;

import network.UMISSRest;

public class LoginActivity extends AppCompatActivity {

    public static final String IS_LOGGED = "isLogged";
    public static final String TOKEN = "token";
    private String LOGIN_REQUEST = "api-auth-token/" ;
    private String CONNECTION_ERROR = "Server is offline!";

    private EditText userEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button register = (Button) findViewById(R.id.button_register);
        register.setOnClickListener(registerOnClickListener);
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

        UMISSRest.login(UMISSRest.getAbsoluteURL(LOGIN_REQUEST), jsonObject, getApplicationContext(), new FutureCallback<JsonObject> (){
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                Log.d("Login", "tried");

                if ( e instanceof ClassCastException ){

                    loginHasFailed(userEditText, passwordEditText);
                    Log.d("Login", "failed");
                }else {

                    try {

                        if (result.has("token")) {

                            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                            pushLoginCredentials(sharedPreferences, result.get(TOKEN).getAsString());
                            System.out.println("token;:: " + result.get(TOKEN).getAsString());
                            startMainActivity();
                            Log.d("Login", "success");
                        }else{

                            Log.d("Login", result.toString());
                        }
                    } catch (Exception x) {

                        Toast.makeText(getApplicationContext(), CONNECTION_ERROR, Toast.LENGTH_LONG).show();
                        Log.d("Login", "Connection error");
                    }
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

    private void pushLoginCredentials(SharedPreferences sharedPreferences, String token) {

        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString(IS_LOGGED, "logged");
        prefEditor.commit();
        //TODO: in case this token is necessary
        prefEditor.putString(TOKEN, token);
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

    /* on clicks */

    private View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }
    };
}
