package com.dsa.lupiapp.api;

import retrofit2.Retrofit;

public class ConfigApi {

    public static final String baseUrlE = "http://10.0.2.2:9090";
    public static Retrofit retrofit;
    private static String token = "";

    static {
        initClient();
    }

    private static void initClient() {
    }

}
