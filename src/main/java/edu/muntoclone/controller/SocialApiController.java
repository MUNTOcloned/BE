package edu.muntoclone.controller;

import edu.muntoclone.dto.SocialDetailsResponse;
import edu.muntoclone.dto.SocialMembersResponse;
import edu.muntoclone.dto.SocialRegisterRequest;
import edu.muntoclone.entity.Social;
import edu.muntoclone.repository.ParticipationRepository;
import edu.muntoclone.security.PrincipalDetails;
import edu.muntoclone.service.ParticipationService;
import edu.muntoclone.service.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final ParticipationService participationService;
    private final ParticipationRepository participationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/categories/{id}/socials", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void registerSocial(
            @PathVariable Long id,
            SocialRegisterRequest socialRegisterRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        socialService.registerSocial(id, socialRegisterRequest, principalDetails);
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
    public SocialDetailsResponse socialDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        final boolean isParticipate = socialService.isParticipate(id, principalDetails);
        final Social social = socialService.findById(id);
        return SocialDetailsResponse.of(social, isParticipate);
    }

    @GetMapping("/socials/{id}/members")
    public SocialMembersResponse findAllSocialMembers(
            @PathVariable Long id,
            @RequestParam Integer approved) {
        return socialService.findMembersBySocialId(id, approved);
    }

    @GetMapping("/categories/{id}/socials")
    public Page<SocialDetailsResponse> findAllSocials(
            @PathVariable Long id,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return socialService.findAllByCategoryId(id, pageable)
                .map(s -> SocialDetailsResponse.of(s, false));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/socials/{id}/participation")
    public void participate(@PathVariable Long id,
                            @RequestBody String answer,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        socialService.participate(id, answer, principalDetails);
    }
}