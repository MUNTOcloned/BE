package edu.muntoclone.controller;

import edu.muntoclone.dto.SocialDetailsResponse;
import edu.muntoclone.dto.SocialMembersResponse;
import edu.muntoclone.dto.SocialRegisterRequest;
import edu.muntoclone.entity.Member;
import edu.muntoclone.entity.Participation;
import edu.muntoclone.entity.Social;
import edu.muntoclone.repository.ParticipationRepository;
import edu.muntoclone.security.PrincipalDetails;
import edu.muntoclone.service.ParticipationService;
import edu.muntoclone.service.SocialService;
import edu.muntoclone.type.RecruitmentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialApiController {

    private final SocialService socialService;
    private final ParticipationService participationService;
    private final ParticipationRepository participationRepository;

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
        // TODO: 2022-06-19 서비스 로직으로 분리해야 함.
        final Member owner = socialService.findById(id).getOwner();
        final List<Participation> participationList = participationService.findAllBySocialId(id)
                .stream().filter(p -> p.getApprovedStatus().equals(1))
                .collect(toList());
        final List<Member> members = participationList.stream()
                .map(Participation::getMember)
                .collect(toList());

        final List<SocialMembersResponse.SocialMember> socialMemberResponses =
                members.stream()
                        .map(SocialMembersResponse.SocialMember::of)
                        .collect(toList());

        return SocialMembersResponse.builder()
                .owner(SocialMembersResponse.SocialMember.of(owner))
                .members(socialMemberResponses)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/socials/{id}/participation")
    public void participate(@PathVariable Long id,
                            @RequestBody String answer,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Social social = socialService.findById(id);
        final Member participationMember = principalDetails.getMember();
        int approvedStatus = 0;

        // TODO: 2022-06-19 서비스 로직으로 분리해야 함.
        final int participationHeadcount = participationService.findAllBySocialId(social.getId())
                .size();
        // 소셜링의 참여 인원이 꽉찼으면 거부
        if (participationHeadcount >= social.getLimitHeadcount()) {
            // 참여 불가능 -> Exception
        }

        if (social.getRecruitmentType() == RecruitmentType.EARLY_BIRD) {
            approvedStatus = 1;
        }

        final Participation participation = Participation.builder()
                .member(participationMember)
                .social(social)
                .answer(answer)
                .approvedStatus(approvedStatus)
                .build();

        participationRepository.save(participation);
    }
}