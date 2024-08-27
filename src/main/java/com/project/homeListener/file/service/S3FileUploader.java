package com.project.homeListener.file.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.homeListener.file.controller.FileController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileUploader {

    private final AmazonS3 s3client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final Integer expirationTimeMillis = 1000 * 60 * 60 * 3;

    public URL generatePresignedUrl(Long userId) {
        try {
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

            String fileUrl = userId + "_" + timeStamp + "_" + UUID.randomUUID();

            Date expiration = new Date(date.getTime() + expirationTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileUrl)
                    .withMethod(HttpMethod.PUT)
                    .withExpiration(expiration);

            generatePresignedUrlRequest.addRequestParameter(
                    Headers.S3_CANNED_ACL,
                    CannedAccessControlList.PublicRead.toString()); // public read

            return s3client.generatePresignedUrl(generatePresignedUrlRequest);
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }
    }
}
