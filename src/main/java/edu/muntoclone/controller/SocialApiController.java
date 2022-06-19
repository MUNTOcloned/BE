package edu.muntoclone.controller;

import edu.muntoclone.dto.SocialDetailsResponse;
import edu.muntoclone.dto.SocialRegisterRequest;
import edu.muntoclone.entity.Social;
import edu.muntoclone.security.PrincipalDetails;
import edu.muntoclone.service.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialApiController {

    private final SocialService socialService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/socials", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void registerSocial(SocialRegisterRequest socialRegisterRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        socialService.registerSocial(socialRegisterRequest, principalDetails);
    }

    @PatchMapping(value = "/socials/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateSocial(@PathVariable Long id) {
        // TODO document why this method is empty
    }

    @DeleteMapping("/socials/{id}")
    public void deleteSocial(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Long socialOwnerId = socialService.findById(id).getOwner().getId();
        final Long loginMemberId = principalDetails.getMember().getId();

        if (socialOwnerId.equals(loginMemberId))
            throw new IllegalArgumentException("You are not the owner of social.");

        socialService.deleteById(id);
    }

    @GetMapping("/socials/{id}")
    public SocialDetailsResponse socialDetail(@PathVariable Long id) {
        final Social social = socialService.findById(id);
        return SocialDetailsResponse.of(social);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/socials/{id}/participation")
    public void participate(@PathVariable Long id) {
        // TODO document why this method is empty
    }
}