package com.interview.skeletons.config;

public class ApiConfig {
    public static final String VERSION = "/v1";
    public static final String COMMON_PATH = "/safe-box-mgmt-api";
    public static final String BASE_PATH = COMMON_PATH + VERSION;

    private ApiConfig() {
        throw new UnsupportedOperationException("This class should never be instantiated");
    }
}
