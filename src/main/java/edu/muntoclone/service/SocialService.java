package edu.muntoclone.service;

import edu.muntoclone.aws.AwsS3BucketService;
import edu.muntoclone.aws.AwsS3FileUploadType;
import edu.muntoclone.dto.SocialRegisterRequest;
import edu.muntoclone.entity.Category;
import edu.muntoclone.entity.Member;
import edu.muntoclone.entity.Social;
import edu.muntoclone.repository.SocialRepository;
import edu.muntoclone.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SocialService {

    // TODO: 2022-06-18 의문점: 서비스에서 다른 서비스를 호출해도 되는지?
    private final SocialRepository socialRepository;
    private final CategoryService categoryService;
    private final AwsS3BucketService awsS3BucketService;

    @Transactional
    public void registerSocial(SocialRegisterRequest socialRegisterRequest,
            PrincipalDetails principalDetails) {

        final Member owner = principalDetails.getMember();
        final Category category = categoryService.findById(socialRegisterRequest.getCategoryId());
        final String imageUrl = awsS3BucketService.uploadFile(
                socialRegisterRequest.getImageFile(), AwsS3FileUploadType.SOCIAL
        );

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
}
