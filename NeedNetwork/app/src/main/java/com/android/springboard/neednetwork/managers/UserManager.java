package com.android.springboard.neednetwork.managers;

import android.content.Context;

import com.android.springboard.neednetwork.constants.Constants;
import com.android.springboard.neednetwork.models.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shoeb on 3/27/2017.
 */

public class UserManager {

    private Context mContext;

    public UserManager(Context context) {
        mContext = context;
    }

    public void loginUser(String userName, String password) {

    }

    public void registerUser(User user) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(user));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.REST_API_REGISTER_USER, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        queue.add(jsObjRequest);
    }
}
