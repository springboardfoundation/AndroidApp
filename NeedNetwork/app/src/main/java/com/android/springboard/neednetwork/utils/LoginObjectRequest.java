package com.android.springboard.neednetwork.utils;

import com.android.springboard.neednetwork.constants.Constants;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by usman on 13/01/18.
 */

public class LoginObjectRequest extends JsonRequest<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;

    public LoginObjectRequest(int method, String url, JSONObject jsonRequest,
                              Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), reponseListener,
                errorListener);
        this.listener = reponseListener;
        this.params = params;
    }


    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            final String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put(Constants.RESPONSE_DATA, new JSONArray(jsonString));
            jsonResponse.put(Constants.RESPONSE_HEADERS, new JSONObject(response.headers));
            return Response.success(jsonResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }


}
