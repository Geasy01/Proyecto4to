package com.example.proyecto4to.Otros;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonRequest {
    private static SingletonRequest mySingleton;
    private RequestQueue requestQueue;
    private static Context mctx;

    private SingletonRequest(Context context) {
        this.mctx = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized SingletonRequest getInstance(Context context) {
        if(mySingleton == null) {
            mySingleton = new SingletonRequest(context);
        }
        return mySingleton;
    }

    public<T> void addToRequestQueue(Request<T> request) { requestQueue.add(request); }
}
