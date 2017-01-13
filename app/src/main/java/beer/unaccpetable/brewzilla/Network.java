package beer.unaccpetable.brewzilla;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by zak on 1/12/2017.
 */

public class Network {
    private static Network mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private Network(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Network getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Network(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
