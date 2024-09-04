package com.aungPyae22.demoFileHandling.controller;

import com.aungPyae22.demoFileHandling.service.IFileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(path = "/file")
public class FileController {

    private IFileService iFileService;

    @Autowired
    public FileController(IFileService iFileService){
        this.iFileService = iFileService;
    }

    @Value("${project.poster}")
    private String path;

    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadFileHandaler(@RequestPart MultipartFile file) throws IOException {

        String uploadFileName = iFileService.uploadFile(path, file);
        return ResponseEntity.ok("uploaded file "+uploadFileName);
    }

    @GetMapping(path = "/{fileName}")
    public void serveFileHandaler(@PathVariable("fileName") String name, HttpServletResponse response) throws IOException, FileNotFoundException{
        InputStream file = iFileService.getResourceFile(path, name);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(file, response.getOutputStream());
    }
}
