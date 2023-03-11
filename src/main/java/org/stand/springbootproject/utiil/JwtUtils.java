package org.stand.springbootproject.utiil;

import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils {
    public static String getJwtFromRequest(HttpServletRequest request) {
        // Extract the auth header from request
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the token from auth header
            return authHeader.substring(7);
        }
        return null;
    }
}
