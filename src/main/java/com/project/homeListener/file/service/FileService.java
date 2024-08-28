package com.project.homeListener.file.service;

import com.project.homeListener.file.util.S3FileUploader;
import com.project.homeListener.login.jwt.entity.UserInformInToken;
import com.project.homeListener.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3FileUploader s3FileUploader;

    private final MailService mailService;


    public URL getPresignedUrl(UserInformInToken userInform) throws URISyntaxException {
        URL presignedUrl = s3FileUploader.generatePresignedUrl(userInform.getId());

        URI presignedUri = presignedUrl.toURI();
        URI queryRemoved = new URI(presignedUri.getScheme(), presignedUri.getAuthority(), presignedUri.getPath(), null, null);

        mailService.sendRecordingFiles(userInform, queryRemoved.toString());

        return presignedUrl;
    }


}
