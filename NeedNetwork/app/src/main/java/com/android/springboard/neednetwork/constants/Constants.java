package com.android.springboard.neednetwork.constants;

import com.android.springboard.neednetwork.BuildConfig;

/**
 * Created by Shouib on 6/3/2017.
 */

public class Constants {

    public static final String HTTP_URL = "http://" + BuildConfig.IP_ADDRESS + ":8080/";
    //public static final String HTTP_URL = "http://neednetwork-neednet.7e14.starter-us-west-2.openshiftapps.com/";

    public static final String REST_API_LOGIN_USER = HTTP_URL + "rest/login";
    public static final String REST_API_REGISTER_USER = HTTP_URL + "rest/register";
    public static final String REST_API_CREATE_NEED = HTTP_URL + "rest/%s/needs/";
    public static final String REST_API_UPDATE_NEED = HTTP_URL + "rest/%s/needs/%s";
    public static final String REST_API_REGISTERED_USERS = HTTP_URL + "rest/users";
    public static final String REST_API_OTHERS_NEEDS = HTTP_URL + "rest/%s/needs/others";
    public static final String REST_API_UPDATE_NEED_USERS = HTTP_URL + "rest/%s/needs/%s/updateUsers";

    //Need Tab Titles
    public static final String TAB_MY_NEEDS = "MY NEEDS";
    public static final String TAB_CURRENT_NEEDS = "OTHER'S NEEDS";


    public static final String RESPONSE_DATA = "data";
    public static final String RESPONSE_HEADERS = "headers";
    public static final String HEADER_AUTHORIZATION = "Authorization";

}
