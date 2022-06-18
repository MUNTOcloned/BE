package edu.muntoclone.jwt;

import edu.muntoclone.exception.AuthenticationException;
import edu.muntoclone.security.PrincipalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtCheckFilter extends BasicAuthenticationFilter {

    private final PrincipalService principalService;

    public JwtCheckFilter(AuthenticationManager authenticationManager, PrincipalService principalService) {
        super(authenticationManager);
        this.principalService = principalService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = bearer.substring("Bearer ".length());
        VerifyResult verifyResult = JwtUtils.verify(token);

        if (verifyResult.isSuccess()) {
            log.info("인증이 완료된 사용자 = {}", verifyResult.getUsername());
            UserDetails principalDetails = principalService.loadUserByUsername(verifyResult.getUsername());

            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    principalDetails, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(userToken);
            chain.doFilter(request, response);

        } else {
            throw new AuthenticationException("Token is not valid");
        }
    }
}