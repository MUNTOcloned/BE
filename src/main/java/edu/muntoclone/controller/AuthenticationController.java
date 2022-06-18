package edu.muntoclone.controller;

import edu.muntoclone.dto.MemberLoginRequest;
import edu.muntoclone.dto.MemberSignupRequest;
import edu.muntoclone.entity.Member;
import edu.muntoclone.jwt.JwtToken;
import edu.muntoclone.jwt.JwtUtils;
import edu.muntoclone.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입 API", description = "ContentType: multipart/form-data")
    public void signup(MemberSignupRequest memberSignupRequest) {
        memberService.confirmMemberEmailDuplicate(memberSignupRequest.getEmail());
        memberService.signup(
                memberSignupRequest.toEntity(passwordEncoder),
                memberSignupRequest.getProfileImage()
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "로그인 API")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JwtToken login(
            @RequestBody MemberLoginRequest memberLoginRequest, HttpServletResponse response) {

        final String email = memberLoginRequest.getEmail();
        final String rawPassword = memberLoginRequest.getPassword();

        final Member member = memberService.findByUsername(email);
        if (!passwordEncoder.matches(rawPassword, member.getPassword()))
            throw new IllegalArgumentException("Please check your email or password");

        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());

        final Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwtToken = JwtUtils.makeAuthToken(member);

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

        return JwtToken.builder()
                .token(jwtToken)
                .expiresAt(JwtUtils.AUTH_TIME_SECOND)
                .build();
    }
}