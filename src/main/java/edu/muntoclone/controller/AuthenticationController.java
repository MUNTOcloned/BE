package edu.muntoclone.controller;

import edu.muntoclone.dto.MemberSignupRequest;
import edu.muntoclone.service.MemberService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입 API", description = "ContentType: multipart/form-data")
    @ApiResponses(@ApiResponse(code = 201, message = "회원가입에 성공했습니다."))
    public void signup(MemberSignupRequest memberSignupRequest) {
        memberService.confirmMemberEmailDuplicate(memberSignupRequest.getEmail());
        memberService.signup(
                memberSignupRequest.toEntity(passwordEncoder),
                memberSignupRequest.getProfileImage()
        );
    }
}
