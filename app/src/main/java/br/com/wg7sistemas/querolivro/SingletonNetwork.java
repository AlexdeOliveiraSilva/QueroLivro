package br.com.wg7sistemas.querolivro;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by alexdeoliveirasilva on 03/06/17.
 */

public class SingletonNetwork {
    private static SingletonNetwork mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    public static  String URLserver = "http://www.wg7sistemas.com.br/alex/querolivro/?comando=";

    private SingletonNetwork(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized SingletonNetwork getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonNetwork(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
