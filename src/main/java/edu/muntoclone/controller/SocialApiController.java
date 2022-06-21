package edu.muntoclone.controller;

import edu.muntoclone.dto.SocialDetailsResponse;
import edu.muntoclone.dto.SocialMembersResponse;
import edu.muntoclone.dto.SocialModifyRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * 소셜 등록 API
     *
     * @param id                    카테고리 번호
     * @param socialRegisterRequest 소셜 등록 DTO
     * @param principalDetails      로그인 사용자 정보
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/categories/{id}/socials", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void registerSocial(
            @PathVariable Long id,
            SocialRegisterRequest socialRegisterRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        socialService.registerSocial(id, socialRegisterRequest, principalDetails);
    }

    /**
     * 소셜 수정 API
     *
     * @param id 소셜 번호
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PatchMapping(value = "/socials/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void modifySocial(
            @PathVariable Long id,
            @RequestBody SocialModifyRequest socialModifyRequest) {
        socialService.modify(id, socialModifyRequest);
    }

    /**
     * 소셜 삭제 API
     *
     * @param id               소셜 번호
     * @param principalDetails 로그인 사용자 정보
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @DeleteMapping("/socials/{id}")
    public void deleteSocial(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Long socialOwnerId = socialService.findById(id).getOwner().getId();
        final Long loginMemberId = principalDetails.getMember().getId();

        if (socialOwnerId.equals(loginMemberId))
            throw new IllegalArgumentException("You are not the owner of social.");

        socialService.deleteById(id);
    }

    /**
     * 소셜 상세 조회 API
     *
     * @param id               소셜 번호
     * @param principalDetails 로그인 사용자 정보
     * @return 소셜 상세 DTO
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/socials/{id}")
    public SocialDetailsResponse showSocialDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        final boolean isParticipate = socialService.isParticipate(id, principalDetails);
        final Social social = socialService.findById(id);
        return SocialDetailsResponse.of(social, isParticipate);
    }

    /**
     * 소셜에 참여신청한 회원 목록 조회 API
     * approved가 1인 경우 참여 확정 멤버를 응답하고 0인 경우엔 참여 대기 멤버를 응답한다.
     *
     * @param id       소셜 번호
     * @param approved 소셜 참여 여부 ( 1 = 참여, 0 미참여 )
     * @return 회원 목록 DTO
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/socials/{id}/members")
    public SocialMembersResponse showSocialMembers(
            @PathVariable Long id,
            @RequestParam Integer approved) {
        return socialService.findMembersBySocialId(id, approved);
    }

    /**
     * 전체 소셜링 조회 API
     *
     * @param id 카테고리 번호
     * @return 전체 소셜링 목록 DTO
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/categories/{id}/socials")
    public Page<SocialDetailsResponse> showSocials(
            @PathVariable Long id,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return socialService.findAllByCategoryId(id, pageable)
                .map(s -> SocialDetailsResponse.of(s, false));
    }

    /**
     * 소셜링 참여 API
     *
     * @param id               소셜 번호
     * @param answer           참여 질문의 대답
     * @param principalDetails 로그인 사용자 정보
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/socials/{id}/participation")
    public void participate(@PathVariable Long id,
                            @RequestBody String answer,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        socialService.participate(id, answer, principalDetails);
    }

    /**
     * 소셜링 회원 승인 API
     *
     * @param sid 소셜 번호
     * @param mid 회원 번호
     * @param principalDetails 로그인 사용자 정보
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PostMapping("/socials/{sid}/members/{mid}/approved")
    public void approve(@PathVariable Long sid, @PathVariable Long mid,
                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        participationService.approve(sid, mid, principalDetails);
    }

}