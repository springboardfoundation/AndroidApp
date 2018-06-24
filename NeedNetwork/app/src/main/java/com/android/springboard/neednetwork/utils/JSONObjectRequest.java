package com.android.springboard.neednetwork.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by usman on 13/01/18.
 */

public class JSONObjectRequest extends JsonRequest<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;

    public JSONObjectRequest(int method, String url, JSONObject jsonRequest,
                              Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), reponseListener,
                errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public JSONObjectRequest(int method, String url, String jsonRequest,
                             Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest, reponseListener,
                errorListener);
        this.listener = reponseListener;
        this.params = params;
    }


    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    ;

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            final String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(response));
        } catch (Exception je) {
            return Response.error(new ParseError(response));
        }
    }
}