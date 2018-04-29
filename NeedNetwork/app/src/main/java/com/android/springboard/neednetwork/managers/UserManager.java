package com.android.springboard.neednetwork.managers;

import android.content.Context;
import android.util.Base64;

import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.constants.Constants;
import com.android.springboard.neednetwork.models.User;
import com.android.springboard.neednetwork.utils.JSONArrayRequest;
import com.android.springboard.neednetwork.utils.JSONObjectRequest;
import com.android.springboard.neednetwork.utils.LoginObjectRequest;
import com.android.springboard.neednetwork.utils.SharedPrefsUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Shoeb on 3/27/2017.
 */

public class UserManager {

    private Context mContext;

    public UserManager(Context context) {
        mContext = context;
    }

    public void login(User user, Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.getCache().clear();
        Gson gson = new Gson();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(gson.toJson(user));
        }catch(JSONException exp) {
            exp.printStackTrace();
        }

        LoginObjectRequest jsObjRequest = new LoginObjectRequest(Request.Method.POST, Constants.REST_API_LOGIN_USER, jsonObject,jsonObjectListener, errorListener);
        queue.add(jsObjRequest);
    }

    public void registerUser(User user, Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.getCache().clear();
        Gson gson = new Gson();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(gson.toJson(user));
        }catch(JSONException exp) {
            exp.printStackTrace();
        }

        JSONObjectRequest jsObjRequest = new JSONObjectRequest(Request.Method.POST, Constants.REST_API_REGISTER_USER, jsonObject,jsonObjectListener, errorListener);
        queue.add(jsObjRequest);
    }

    public JSONArray fetchRegisteredUsers(List<String> contacts) throws InterruptedException, ExecutionException, TimeoutException {
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JSONArray jsonArray = new JSONArray(contacts);

        String mobileNumber = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_MOBILE_NUMBER);
        String url = String.format(Constants.REST_API_REGISTERED_USERS, mobileNumber);
        JSONArrayRequest jsObjRequest = new JSONArrayRequest(Request.Method.POST, url, String.valueOf(jsonArray),future, future) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String token = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_TOKEN);
                params.put(Constants.HEADER_AUTHORIZATION, new String(Base64.decode(token, Base64.DEFAULT)));
                return params;
            }
        };

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 15, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsObjRequest.setTag(Calendar.getInstance().getTimeInMillis());
        queue.add(jsObjRequest);

        return future.get(1, TimeUnit.MINUTES);
    }
}
