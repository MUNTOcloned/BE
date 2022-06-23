package edu.muntoclone.service;

import edu.muntoclone.aws.AwsS3BucketService;
import edu.muntoclone.aws.AwsS3FileUploadType;
import edu.muntoclone.entity.Member;
import edu.muntoclone.exception.DuplicateUsernameException;
import edu.muntoclone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AwsS3BucketService awsS3BucketService;

    @Transactional
    public void signup(Member member, MultipartFile profileImageFile) {
        if (!profileImageFile.isEmpty()) {
            final String profileImageUrl = awsS3BucketService.uploadFile(
                    profileImageFile, AwsS3FileUploadType.PROFILE
            );

            member.setProfileImageUrl(profileImageUrl);
        }

        memberRepository.save(member);
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("[" + username + "] does not exist."));
    }

    public void confirmMemberEmailDuplicate(String email) {
        Optional<Member> findMember = memberRepository.findByUsername(email);
        if (findMember.isPresent()) {
            throw new DuplicateUsernameException("[" + email + "] already exists.");
        }
    }
}