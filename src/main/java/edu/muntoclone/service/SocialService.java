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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SocialService {

    // TODO: 2022-06-18 의문점: 서비스에서 다른 서비스를 호출해도 되는지?
    private final SocialRepository socialRepository;
    private final CategoryService categoryService;
    private final AwsS3BucketService awsS3BucketService;

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
}
