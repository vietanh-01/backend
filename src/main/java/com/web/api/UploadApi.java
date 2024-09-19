package com.web.api;
import com.web.dto.response.UploadResponse;
import com.web.utils.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UploadApi {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/public/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file){
        try {
            return cloudinaryService.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/public/upload-multiple-file")
    public List<String> uploadFile(@RequestParam("file") List<MultipartFile> file){
        List<String> list = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i=0; i<file.size(); i++) {
            Integer x=i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String image = cloudinaryService.uploadFile(file.get(x));
                        list.add(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        es.shutdown();
        try {
            boolean finished = es.awaitTermination(100000, TimeUnit.MINUTES);
            if (finished) {
                return list;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }


    @PostMapping("/public/upload-multiple-file-order-response")
    public List<UploadResponse> uploadFileUploadResponses(@RequestParam("file") List<MultipartFile> file){
        List<UploadResponse> list = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i=0; i<file.size(); i++) {
            Integer x=i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String image = cloudinaryService.uploadFile(file.get(x));
                        UploadResponse obj = new UploadResponse();
                        obj.setId(x);
                        obj.setLink(image);
                        list.add(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        es.shutdown();
        try {
            boolean finished = es.awaitTermination(100000, TimeUnit.MINUTES);
            if (finished) {
                return list;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Collections.sort(list, new Comparator<UploadResponse>() {

            @Override
            public int compare(UploadResponse o1, UploadResponse o2) {
                return o1.getId() - o2.getId();
            }
        });
        return list;
    }
}
