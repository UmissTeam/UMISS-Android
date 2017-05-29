package com.umiss;

import android.app.ProgressDialog;
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

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Enviando informações");
        progressDialog.show();

        UMISSRest.register(UMISSRest.getAbsoluteURL("api/monitors"), getApplicationContext(), monitor, new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if ( result != null ) {

                    if ( result.has("errors") ){

                        Toast.makeText(getApplicationContext(), "Usuário já existe", Toast.LENGTH_LONG).show();
                        clearFields();
                        progressDialog.dismiss();
                    }else{

                        Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        startLoginActivity();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Erro de conexão.", Toast.LENGTH_LONG).show();
                    Log.d("Register2", e.toString());
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void startLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validatesInput(String user, String password, String passwordConfirmation, String chair){

        boolean isValidated = true;

        if ( !password.equals(passwordConfirmation) ){

            this.password.setError("As senhas devem coincidir");
            this.passwordConfirmation.setText("");
            isValidated = false;
        }else if ( password.equals("") ){

            this.password.setError("Campo não pode ser nulo");
            isValidated = false;
        }

        if ( user.equals("") ){

            this.user.setError("Campo não pode ser nulo");
            isValidated = false;
        }

        if ( chair.equals("") ){

            this.chairToken.setError("Campo não pode ser nulo");
            isValidated = false;
        }

        return isValidated;
    }

    private void clearFields(){

        user.setText("");
        user.requestFocus();
        password.setText("");
        passwordConfirmation.setText("");
        chairToken.setText("");
    }

    /* on clicks */

    private View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userName = user.getText().toString();
            String passwordString = password.getText().toString();
            String passwordConfirmationString = passwordConfirmation.getText().toString();
            String chairTokenString = chairToken.getText().toString();

            if ( validatesInput(userName, passwordString, passwordConfirmationString, chairTokenString) )
                sendInformation(userName, passwordString, chairTokenString);

            Log.d("Register", "Clicked!");
        }
    };


}
