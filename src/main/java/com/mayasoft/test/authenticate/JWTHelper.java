package com.mayasoft.test.authenticate;

import com.mayasoft.test.models.entities.User;
import com.sun.istack.NotNull;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.*;

public class JWTHelper {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS512;
    private static final String CLAIM_USER = "user";
    private static final String CLAIM_TOKEN = "currentToken";

    private static String KEY = "L0R4T4D1N4";

    public static void parseJWT(String token) {
        try {
            Claims claims = (Claims)Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            List<String> userRoles = new ArrayList();
            String userRolesStr = (String)claims.get("roles");
            if (userRolesStr != null && userRolesStr.length() > 2) {
                userRolesStr = userRolesStr.substring(1, userRolesStr.length() - 1);
                userRoles.addAll(Arrays.asList(userRolesStr.split(",")));
            }

        } catch (ExpiredJwtException var4) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        } catch (SignatureException var5) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature verification failed");
        } catch (Exception var6) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not able to parse authentication token");
        }

    }

    public static String createJWT(@NotNull User user, Integer hours, String token) {
        JwtBuilder builder = null;
        try {
            Date now = new Date(System.currentTimeMillis());

            // Let's set the JWT Claims
            builder = Jwts
                    .builder()
                    .setIssuedAt(now)
                    .setSubject(user.getInstructor().getId().toString())
                    .claim(CLAIM_USER, user.getUserName())
                    .signWith(SIGNATURE_ALGORITHM, KEY)
                    .setExpiration(calculateTTL(now, hours));
            if(token != null) builder.claim(CLAIM_TOKEN, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    // Calculates time to live.
    private static Date calculateTTL(Date date, Integer hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours);
        return cal.getTime();
    }

    public static String extractToken(String auth, String scheme) {
        // Replacing "<scheme> token" to "token" directly.
        return auth.replaceFirst(scheme + " ", "");
    }


    public static String getKEY() {
        return KEY;
    }
}
