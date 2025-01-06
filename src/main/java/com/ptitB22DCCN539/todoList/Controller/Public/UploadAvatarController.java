package com.ptitB22DCCN539.todoList.Controller.Public;

import com.ptitB22DCCN539.todoList.Service.Public.Upload.UploadFileGoogleDriver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/upload")
public class UploadAvatarController {
    private final UploadFileGoogleDriver uploadFileGoogleDriver;

    @PostMapping(value = "/")
    public String uploadAvatar(MultipartFile file) throws IOException {
        File uploadFile = new File("photo.jpg");
        return uploadFileGoogleDriver.uploadBasic(uploadFile);
    }
}
