package com.jozoppi.security;

public class SecurityConst
{
    /**
     * Security constant for the authentication.
     * TODO: Store them in a secure memory.
     */
    public static final String SECRET = "Ingenico";
    public static final long EXPIRATION_TIME = 10800000; // 3 hours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";

}
