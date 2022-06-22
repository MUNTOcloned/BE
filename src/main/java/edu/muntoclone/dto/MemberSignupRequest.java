package edu.muntoclone.dto;

import edu.muntoclone.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignupRequest {

    private String name;
    private String email;
    private String password;
    private MultipartFile profileImageFile;

    @Builder
    public MemberSignupRequest(String name, String email, String password, MultipartFile profileImageFile) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageFile = profileImageFile;
    }

    public Member toEntity(BCryptPasswordEncoder passwordEncoder) {
        return Member.builder()
                .username(this.email)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .build();
    }
}
