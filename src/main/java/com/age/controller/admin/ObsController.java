package com.age.controller.admin;

import com.age.service.OBSService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/sys/ahuser")
public class ObsController {

    @Autowired
    private OBSService obsService;

    @PostMapping("/upload")
    public String UploadImage(@RequestParam("file") MultipartFile file)
    {
        String result = obsService.uploadFile(file);

        return result;//ResponseEntity.ok(result);
    }
}


