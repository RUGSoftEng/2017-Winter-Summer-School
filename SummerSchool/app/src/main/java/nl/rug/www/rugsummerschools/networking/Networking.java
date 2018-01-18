package nl.rug.www.rugsummerschools.networking;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Networking {

    private static final String TAG = "Networking";
    private static final String HTTPURL = "turing13.housing.rug.nl:8800";

    private RequestQueue mRequestQueue;

    interface VolleyCallback<T> {
        void onResponse(T result);
        void onError(String result);
    }

    public Networking(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    private String buildURL(List<String> paths, Map<String, String> queryParams) {
        Uri.Builder builder = new Uri.Builder();
        builder
                .scheme("http")
                .encodedAuthority(HTTPURL);
//                .appendPath("API"); // Calendar API not included

        if (paths != null) {
            for (String s : paths) {
                builder.appendPath(s);
            }
        }

        if (queryParams != null) {
            Iterator it = queryParams.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();
                builder.appendQueryParameter(key, queryParams.get(key));
                it.remove();
            }
        }

        String url = builder.toString();
        Log.d(TAG, "url : " + url);
        return url;
    }

    public void getJSONObjectRequest(List<String> paths, Map<String, String> queryParams, final VolleyCallback<JSONObject> callback) {
        String url = buildURL(paths, queryParams);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Get request Error : " + error.toString());
                        callback.onError(error.toString());
                    }
                });
        mRequestQueue.add(request);
    }

    public void getJSONArrayRequest(List<String> paths, Map<String, String> queryParams, final VolleyCallback<JSONArray> callback) {
        String url = buildURL(paths, queryParams);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Get request Error : " + error.toString());
                        callback.onError(error.toString());
                    }
                });
        mRequestQueue.add(request);
    }

    public void getDeleteRequest(int method, List<String> paths, Map<String, String> queryParams, final Map<String, String> valuePairs, final VolleyCallback<String> callback) {
        String url = buildURL(paths, queryParams);
        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                });
        mRequestQueue.add(request);
    }

    public void postPutRequest(int method, List<String> paths, Map<String, String> queryParams, final Map<String, String> valuePairs, final VolleyCallback<String> callback) {
        String url = buildURL(paths, queryParams);
        StringRequest request = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return valuePairs;
            }
        };
        mRequestQueue.add(request);
    }
}
