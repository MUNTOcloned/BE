package edu.muntoclone.controller;

import edu.muntoclone.dto.SocialDetailsResponse;
import edu.muntoclone.dto.SocialMembersResponse;
import edu.muntoclone.dto.SocialRegisterRequest;
import edu.muntoclone.entity.Member;
import edu.muntoclone.entity.Participation;
import edu.muntoclone.entity.Social;
import edu.muntoclone.security.PrincipalDetails;
import edu.muntoclone.service.ParticipationService;
import edu.muntoclone.service.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialApiController {

    private final SocialService socialService;
    private final ParticipationService participationService;

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

    @GetMapping("/socials/{id}/members")
    public SocialMembersResponse findAllSocialMembers(@PathVariable Long id) {
        final Member owner = socialService.findById(id).getOwner();
        final List<Participation> participationList = participationService.findAllBySocialId(id);
        final List<Member> members = participationList.stream()
                .map(Participation::getMember)
                .collect(Collectors.toList());

        final List<SocialMembersResponse.SocialMember> socialMemberResponses =
                members.stream()
                .map(SocialMembersResponse.SocialMember::of)
                .collect(Collectors.toList());

        return SocialMembersResponse.builder()
                .owner(SocialMembersResponse.SocialMember.of(owner))
                .members(socialMemberResponses)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/socials/{id}/participation")
    public void participate(@PathVariable Long id) {
        // TODO document why this method is empty
    }
}