package network;

import android.content.Context;
import android.provider.Settings;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import models.Monitor;

public class UMISSRest {

    private static final String BASE_URL = Server.URL;

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
    public static void sendAndroidToken(String url, Context context, Monitor monitor, String token,
                                        FutureCallback<JsonObject> futureCallback){

        Ion.with(context).load("PUT",url).setHeader("Authorization","Token "+token).
                setBodyParameter("username", monitor.getUserName()).
                setBodyParameter("password", monitor.getPassword()).
                setBodyParameter("token", monitor.getChairToken()).
                setBodyParameter("android_token", monitor.getAndroidToken()).
                asJsonObject().setCallback(futureCallback);
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
