package edu.muntoclone.service;

import edu.muntoclone.aws.AwsS3BucketService;
import edu.muntoclone.aws.AwsS3FileUploadType;
import edu.muntoclone.dto.SocialMembersResponse;
import edu.muntoclone.dto.SocialModifyRequest;
import edu.muntoclone.dto.SocialRegisterRequest;
import edu.muntoclone.entity.Category;
import edu.muntoclone.entity.Member;
import edu.muntoclone.entity.Participation;
import edu.muntoclone.entity.Social;
import edu.muntoclone.exception.SocialHeadcountLimitException;
import edu.muntoclone.repository.ParticipationRepository;
import edu.muntoclone.repository.SocialRepository;
import edu.muntoclone.security.PrincipalDetails;
import edu.muntoclone.type.MeetingType;
import edu.muntoclone.type.RecruitmentType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SocialService {

    // TODO: 2022-06-18 의문점: 서비스에서 다른 서비스를 호출해도 되는지?
    private final SocialRepository socialRepository;
    private final ParticipationRepository participationRepository;
    private final CategoryService categoryService;
    private final AwsS3BucketService awsS3BucketService;
    private final ParticipationService participationService;

    @Transactional
    public void registerSocial(
            Long categoryId,
            SocialRegisterRequest socialRegisterRequest,
            PrincipalDetails principalDetails) {

        final Member owner = principalDetails.getMember();
        final Category category = categoryService.findById(categoryId);
        final MultipartFile imageFile = socialRegisterRequest.getImageFile();
        final String imageUrl = imageFile.isEmpty() ?
                null : awsS3BucketService.uploadFile(imageFile, AwsS3FileUploadType.SOCIAL);

        final Social social = socialRegisterRequest.toEntity(owner, category, imageUrl);
        socialRepository.save(social);
    }

    @Transactional
    public void deleteById(Long id) {
        socialRepository.deleteById(id);
    }

    public Social findById(Long id) {
        return socialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Social does not exist."));
    }

    public Page<Social> findAllByCategoryId(Long id, Pageable pageable) {
        return socialRepository.findAllByCategoryId(id, pageable);
    }

    @Transactional
    public void participate(Long socialId, String answer, PrincipalDetails principalDetails) {
        final Social social = this.findById(socialId);
        final Member participationMember = principalDetails.getMember();
        int approvedStatus = 0;

        // 소셜링의 Owner라면 참여하지 못한다.
        final Long ownerId = this.findById(socialId).getOwner().getId();
        final Long memberId = participationMember.getId();
        if (ownerId.equals(memberId))
            throw new IllegalArgumentException("The owner can't participate.");

        // 이미 참여 신청을 했다면 참여하지 못해야 한다.
        final boolean alreadyParticipation = participationService.findAllBySocialId(socialId)
                .stream()
                .anyMatch(p -> p.getMember().getId().equals(participationMember.getId()));
        if (alreadyParticipation)
            throw new IllegalArgumentException("You have already applied for participation.");

        // 소셜링의 참여 인원이 전부 찼으면 거부
        final int participationHeadcount = participationService
                .findAllBySocialId(social.getId()).size();

        if (participationHeadcount >= social.getLimitHeadcount())
            throw new SocialHeadcountLimitException("The number of people is full.");

        if (social.getRecruitmentType() == RecruitmentType.EARLY_BIRD)
            approvedStatus = 1;

        final Participation participation = Participation.builder()
                .member(participationMember)
                .social(social)
                .answer(answer)
                .approvedStatus(approvedStatus)
                .build();

        participationRepository.save(participation);
    }

    public SocialMembersResponse findMembersBySocialId(Long socialId, Integer approvedStatus) {
        final Member owner = this.findById(socialId).getOwner();
        final List<Participation> participationList = participationService
                .findAllBySocialId(socialId)
                .stream()
                .filter(p -> p.getApprovedStatus().equals(approvedStatus))
                .filter(p -> !p.getMember().getId().equals(owner.getId()))
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

    public boolean isParticipate(Long socialId, PrincipalDetails principalDetails) {
        final Long ownerId = this.findById(socialId).getOwner().getId();
        final Long memberId = principalDetails.getMember().getId();
        if (ownerId.equals(memberId))
            return true;

        return participationService.findAllBySocialId(socialId)
                .stream()
                .anyMatch(p -> p.getMember().getId().equals(principalDetails.getMember().getId()));
    }

    @Transactional
    public void modify(Long categoryId, Long socialId, SocialModifyRequest socialModifyRequest) {
        final Social social = this.findById(socialId);
        final Category category = categoryService.findById(categoryId);
        final MultipartFile imageFile = socialModifyRequest.getImageFile();
        final String imageUrl = imageFile.isEmpty() ?
                null : awsS3BucketService.uploadFile(imageFile, AwsS3FileUploadType.SOCIAL);

        final List<Participation> participationList =
                participationService.findAllBySocialId(socialId);

        if (participationList.isEmpty()) {
            social.modify(
                    category,
                    socialModifyRequest.getTitle(),
                    socialModifyRequest.getContent(),
                    imageUrl,
                    socialModifyRequest.getStartDate(),
                    socialModifyRequest.getStartTime(),
                    socialModifyRequest.getMeetingType().equals(MeetingType.ONLINE.getValue()) ?
                            MeetingType.ONLINE : MeetingType.OFFLINE,
                    socialModifyRequest.getRecruitmentType().equals(RecruitmentType.APPROVED.getValue()) ?
                            RecruitmentType.APPROVED : RecruitmentType.EARLY_BIRD,
                    socialModifyRequest.getLimitHeadcount(),
                    socialModifyRequest.getEntryFee(),
                    socialModifyRequest.getEntryFeeInfo()
            );
        } else {
            social.modify(
                    category,
                    socialModifyRequest.getTitle(),
                    socialModifyRequest.getContent(),
                    imageUrl,
                    socialModifyRequest.getStartDate(),
                    socialModifyRequest.getStartTime(),
                    socialModifyRequest.getLimitHeadcount()
            );
        }

    }
}