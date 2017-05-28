package network;

import android.content.Context;
import android.provider.Settings;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import models.Monitor;

public class UMISSRest {

    public static final String BASE_URL = Server.URL;
    public static final String MONITORS = BASE_URL + "api/monitors";

    public static void get(String url,String token, FutureCallback<JsonObject> futureCallback, Context context) {

        Ion.with(context).load(url).setHeader("Authorization", "Token "+token).asJsonObject().setCallback(futureCallback);
    }

    public static void post(String url,JsonObject json, Context context,String token, FutureCallback<JsonObject> futureCallback) {

        Ion.with(context).load(url).setHeader("Authorization","Token "+token).setBodyParameter("beats", "123").asJsonObject().setCallback(futureCallback);
                //setJsonObjectBody(json).asJsonObject().setCallback(futureCallback);
        System.out.println("k->"+json.toString());
    }

    public static void login(String url,JsonObject json, Context context, FutureCallback<JsonObject> futureCallback){

        Ion.with(context).load(url).setJsonObjectBody(json).asJsonObject().setCallback(futureCallback);
    }

    //TODO: remove hardcoded
    public static void sendAndroidToken(String url, Context context, JsonObject jsonObject, String token,
                                        FutureCallback<Response<JsonObject>> futureCallback){

        Ion.with(context).load("PUT",url).setHeader("Authorization","Token "+token).
                setBodyParameter("username", jsonObject.get("username").getAsString()).
                setBodyParameter("password", jsonObject.get("password").getAsString()).
                setBodyParameter("android_token",jsonObject.get("android_token").getAsString()).
                setBodyParameter("token", jsonObject.get("token").getAsString()).
                asJsonObject().withResponse().setCallback(futureCallback);
    }

    public static void register(String url, Context context, Monitor monitor, FutureCallback<JsonObject> futureCallback){

        Ion.with(context).load(url).
                setBodyParameter("username", monitor.getUserName()).
                setBodyParameter("password", monitor.getPassword()).
                setBodyParameter("token", monitor.getChairToken()).
                setBodyParameter("android_token", monitor.getAndroidToken()).
                asJsonObject().setCallback(futureCallback);
    }

    public static String getAbsoluteURL(String relativeURL){

        return BASE_URL + relativeURL;
    }
}
