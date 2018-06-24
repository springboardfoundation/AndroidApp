package com.android.springboard.neednetwork.managers;

import android.content.Context;
import android.util.Base64;

import com.android.springboard.neednetwork.constants.ActivityConstants;
import com.android.springboard.neednetwork.constants.Constants;
import com.android.springboard.neednetwork.models.Need;
import com.android.springboard.neednetwork.utils.JSONObjectRequest;
import com.android.springboard.neednetwork.utils.SharedPrefsUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shouib on 9/24/2017.
 */

public class NeedManager {

    private Context mContext;

    public NeedManager(Context context) {
        mContext = context;
    }

    public void createNeed(Need need, Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.getCache().clear();
        Gson gson = new Gson();
        JSONObject jsonObject = null;

        try {
            String json = gson.toJson(need);
            jsonObject = new JSONObject(json);
        }catch(JSONException exp) {
            exp.printStackTrace();
        }

        String mobileNumber = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_MOBILE_NUMBER);
        String url = String.format(Constants.REST_API_CREATE_NEED, mobileNumber);
        JSONObjectRequest jsObjRequest = new JSONObjectRequest(Request.Method.POST, url, jsonObject,jsonObjectListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String token = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_TOKEN);
                params.put(Constants.HEADER_AUTHORIZATION, new String(Base64.decode(token, Base64.DEFAULT)));
                return params;
            }
        };
        queue.add(jsObjRequest);
    }

    public void updateNeed(Need need, Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.getCache().clear();
        Gson gson = new Gson();
        JSONObject jsonObject = null;

        try {
            String json = gson.toJson(need);
            jsonObject = new JSONObject(json);
        }catch(JSONException exp) {
            exp.printStackTrace();
        }

        String mobileNumber = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_MOBILE_NUMBER);
        String url = String.format(Constants.REST_API_UPDATE_NEED, mobileNumber, need.getId());
        JSONObjectRequest jsObjRequest = new JSONObjectRequest(Method.PUT, url, jsonObject,jsonObjectListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String token = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_TOKEN);
                params.put(Constants.HEADER_AUTHORIZATION, new String(Base64.decode(token, Base64.DEFAULT)));
                return params;
            }
        };
        queue.add(jsObjRequest);
    }

    public void updateUsers(Need need, Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.getCache().clear();
        Gson gson = new Gson();
        JSONArray jsonArray = new JSONArray(need.getUsers());

        String mobileNumber = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_MOBILE_NUMBER);
        String url = String.format(Constants.REST_API_UPDATE_NEED_USERS, mobileNumber, need.getId());
        JSONObjectRequest jsObjRequest = new JSONObjectRequest(Method.PUT, url, String.valueOf(jsonArray),jsonObjectListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String token = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_TOKEN);
                params.put(Constants.HEADER_AUTHORIZATION, new String(Base64.decode(token, Base64.DEFAULT)));
                return params;
            }
        };
        queue.add(jsObjRequest);
    }

    public void deleteNeed(Need need, Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(need));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Method.POST, Constants.REST_API_REGISTER_USER, jsonObject, jsonObjectListener, errorListener);

        queue.add(jsObjRequest);
    }

    public void getCurrentNeeds(Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String mobileNumber = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_MOBILE_NUMBER);
        String url = String.format(Constants.REST_API_OTHERS_NEEDS, mobileNumber);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener
                ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String token = SharedPrefsUtils.getStringPreference(mContext, ActivityConstants.PREF_TOKEN);
                params.put(Constants.HEADER_AUTHORIZATION, new String(Base64.decode(token, Base64.DEFAULT)));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void getMyNeeds(Response.Listener<JSONObject> jsonObjectListener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Method.POST, Constants.REST_API_REGISTER_USER, jsonObject, jsonObjectListener, errorListener);

        queue.add(jsObjRequest);
    }



}
