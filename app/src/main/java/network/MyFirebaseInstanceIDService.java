package network;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;
import com.umiss.LoginActivity;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh(){

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("refreshedToken", refreshedToken);

        if ( isLooged() ){

            sendAndroidToken(refreshedToken);
        }else{

            Log.d("onTokenRefresh", "not logged!");
        }
    }

    private boolean isLooged(){

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String isLogged = sharedPreferences.getString(LoginActivity.IS_LOGGED, "notlogged");

        return isLogged.equals("logged");
    }

    private void sendAndroidToken(String androidToken){

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String password = sharedPreferences.getString("password", "id");
        String token = sharedPreferences.getString("token", "token");
        request(password, token, androidToken);
    }

    private void request(final String password, final String token,final String androidToken){

        UMISSRest.get(UMISSRest.MONITORS + "/1", token, new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if ( result != null ) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("username", result.get("data").getAsJsonObject().get("attributes").getAsJsonObject()
                            .get("username").getAsString());
                    jsonObject.addProperty("token", result.get("data").getAsJsonObject().get("attributes").getAsJsonObject()
                            .get("token").getAsString());
                    jsonObject.addProperty("password", password);
                    jsonObject.addProperty("android_token", androidToken);

                    String id = result.get("data").getAsJsonObject().get("id").getAsString();
                    jsonObject.addProperty("id", id);

                    if (result == null)
                        Log.d("LoginActivityget", e.toString());
                    else
                        Log.d("LoginActivityget", result.toString());

                    Log.d("LoginActivityput", UMISSRest.MONITORS + "/" + id);

                    UMISSRest.sendAndroidToken(UMISSRest.MONITORS + "/" + id, getApplicationContext(), jsonObject,
                            token, new FutureCallback<Response<JsonObject>>() {
                                @Override
                                public void onCompleted(Exception e, Response<JsonObject> result) {
                                    Log.d("LoginActivity", String.valueOf(result.getHeaders().code()));

                                    if (result.getHeaders().code() != 200) {

                                        Toast.makeText(getApplicationContext(),
                                                "Não foi possível enviar o token para o servidor, faça login novamente.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {

                    Toast.makeText(getApplicationContext(),
                            "Não foi possível enviar o token para o servidor, faça login novamente.", Toast.LENGTH_LONG).show();
                }
            }
        }, getApplicationContext());
    }
}
