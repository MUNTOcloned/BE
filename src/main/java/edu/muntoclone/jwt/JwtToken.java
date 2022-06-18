package edu.muntoclone.jwt;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtToken {

    private String token;
    private int expiresAt;

    @Builder
    public JwtToken(String token, int expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }
}
