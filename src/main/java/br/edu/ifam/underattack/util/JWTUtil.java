package br.edu.ifam.underattack.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

/**
 * Created by erika.silva on 22/04/2017.
 */
public class JWTUtil {


    private static String JWT_PRIVATE_KEY = "under_attack";

    public static String generateToken(String jwtSubject) {
        return Jwts.builder().setSubject(jwtSubject)
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(JWT_PRIVATE_KEY)).compact();
    }

    public static Claims parseToken(String jwtToken) {
        return Jwts.parser().setSigningKey(TextCodec.BASE64.encode(JWT_PRIVATE_KEY))
                .parseClaimsJws(jwtToken).getBody();
    }


}
