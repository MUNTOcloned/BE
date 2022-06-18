package edu.muntoclone.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import edu.muntoclone.exception.AwsS3FileUploadTypeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AwsS3BucketService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    public String uploadFile(MultipartFile multipartFile, AwsS3FileUploadType type) {
        final String fileName = s3FileUpload(multipartFile, type);
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    public List<String> uploadFiles(List<MultipartFile> multipartFiles, AwsS3FileUploadType type) {
        final List<String> list = new ArrayList<>();
        multipartFiles.forEach(multipartFile -> {
            final String fileName = s3FileUpload(multipartFile, type);
            final String url = amazonS3Client.getUrl(bucketName, fileName).toString();
            list.add(url);
        });
        return list;
    }

    private String createFileName(MultipartFile multipartFile, AwsS3FileUploadType type) {
        if (type == AwsS3FileUploadType.PROFILE) {
            return AwsS3FileUploadType.PROFILE.getPath()
                    + UUID.randomUUID() + multipartFile.getOriginalFilename();
        } else if (type == AwsS3FileUploadType.SOCIAL) {
            return AwsS3FileUploadType.SOCIAL.getPath()
                    + UUID.randomUUID() + multipartFile.getOriginalFilename();
        }
        throw new AwsS3FileUploadTypeNotFoundException();
    }
    private String s3FileUpload(MultipartFile multipartFile, AwsS3FileUploadType type) {
        final String fileName = createFileName(multipartFile, type);
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, fileName, inputStream, metadata)
            );
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed.");
        }
        return fileName;
    }
}