package network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class UMISSRest {

    private static final String BASE_URL = "http://10.0.2.2:8000/";

    public static void get(String url, FutureCallback<JsonObject> futureCallback, Context context) {

        Ion.with(context).load(url).asJsonObject().setCallback(futureCallback);
    }

    public static void post(String url,JsonObject json, Context context, FutureCallback<JsonObject> futureCallback) {

        Ion.with(context).load(url).setJsonObjectBody(json).asJsonObject().setCallback(futureCallback);
    }

    public static String getAbsoluteURL(String relativeURL){

        return BASE_URL + relativeURL;
    }
}
