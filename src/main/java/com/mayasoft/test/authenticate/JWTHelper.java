package com.mayasoft.test.authenticate;

import com.mayasoft.test.models.entities.User;
import com.sun.istack.NotNull;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;

public class JWTHelper {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS512;
    private static final String CLAIM_USER = "user";
    private static final String CLAIM_TOKEN = "currentToken";

    private static String KEY = "L0R4T4D1N4";

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
