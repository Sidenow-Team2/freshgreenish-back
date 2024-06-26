package com.sidenow.freshgreenish.global.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<UploadFile> uploadFileList(List<MultipartFile> multipartFiles, String dir) {
        List<UploadFile> fileList = new ArrayList<>();
        if (multipartFiles == null) {
            return fileList;
        }
        multipartFiles.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, dir + "/" + fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                //파일 DTO 생성
                UploadFile uploadFile = UploadFile.builder()
                        .originFileName(file.getOriginalFilename())
                        .fileName(fileName)
                        .filePath(amazonS3Client.getUrl(bucket, dir + "/" + fileName).toString())
                        .fileSize(file.getSize())
                        .build();

                //생성 후 리스트에 추가
                fileList.add(uploadFile);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패");
            }
        });
        return fileList;
    }

    public UploadFile uploadfile(MultipartFile multipartFile, String dir) throws IOException {
        String fileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getInputStream().available());

        amazonS3Client.putObject(bucket, dir + "/" + fileName, multipartFile.getInputStream(), objectMetadata);

        return UploadFile.builder()
                .originFileName(multipartFile.getOriginalFilename())
                .fileName(fileName)
                .filePath(amazonS3Client.getUrl(bucket, dir + "/" + fileName).toString())
                .fileSize(multipartFile.getSize())
                .build();
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ")");
        }
    }

    public void delete(String fileName, String dir) {
        String key = dir + "/" + fileName;
        amazonS3Client.deleteObject(bucket, key);
    }
}
