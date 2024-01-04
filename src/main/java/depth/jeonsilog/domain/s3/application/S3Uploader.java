package depth.jeonsilog.domain.s3.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {
    // AmazonS3Client -> AmazonS3
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile -> File 전환 후 S3 버킷에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());

        removeNewFile(convertFile);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // S3에서 이미지 삭제
    public void deleteImage(String dirName, String imageUrl) {
        String fileName = extractFileNameFromUrl(imageUrl);

        if (fileName != null) {
            try {
                fileName = URLDecoder.decode(fileName, "UTF-8");
            } catch (Exception e) {
                log.error("파일명 디코딩 중 오류 발생", e);
                return;
            }

            // 디렉토리명과 파일명을 조합하여 전체 객체 키 생성
            String objectKey = dirName + "/" + fileName;

            // S3 파일 확인
            if (isS3FileExists(objectKey)) {
                amazonS3Client.deleteObject(bucket, objectKey);
                log.info("S3 이미지 삭제가 완료되었습니다. 파일명: {}", objectKey);

            } else {
                log.warn("S3에 해당 파일이 존재하지 않습니다. 파일명: {}", objectKey);
            }

        } else {
            log.warn("유효하지 않은 S3 이미지 URL입니다. URL: {}", imageUrl);
        }

    }

    // S3에 해당 파일이 존재하는지 확인
    private boolean isS3FileExists(String fileName) {
        try {
            ObjectMetadata objectMetadata = amazonS3Client.getObjectMetadata(bucket, fileName);
            return true;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                return false; // 파일이 존재하지 않음
            } else {
                throw e; // 다른 예외는 다시 던짐
            }
        }
    }

    private String extractFileNameFromUrl(String imageUrl) {
        try {
            return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        } catch (Exception e) {
            return null;
        }
    }
}
