package com.tresbu.trakeye.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";

    public static final String USER_ADMIN = "ROLE_USER_ADMIN";

    public static final String USER = "ROLE_USER";
    
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
