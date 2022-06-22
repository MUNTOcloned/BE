package edu.muntoclone.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.muntoclone.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("SECRET");
    public static final String JWT_COOKIE_NAME = "JWTToken";
    public static final int AUTH_TIME_SECOND = 60 * 200;
    public static final int AUTH_TIME_MILLIS = 60000 * 200; // 인증시간 20분
    public static final int REFRESH_TIME_MILLIS = 60000 * 60 * 24 * 7; // REFRESH 시간 7일

    public static String makeAuthToken(Member member) {
        return JWT.create()
                .withSubject(member.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + AUTH_TIME_MILLIS))
                .sign(ALGORITHM);
    }

    public static String makeRefreshToken(Member member) {
        return JWT.create()
                .withSubject(member.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TIME_MILLIS))
                .sign(ALGORITHM);
    }

    public static VerifyResult verify(String token) {
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder().success(true)
                    .username(verify.getSubject()).build();

        } catch (Exception e) {
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder().success(false)
                    .username(decode.getSubject()).build();
        }
    }
}
