package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.ImageDto;
import com.uriel.travel.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final S3Service s3Service;

    // 이미지 등록
    @PostMapping
    public BaseResponse<List<ImageDto>> upload(@RequestBody List<MultipartFile> files) {
        return BaseResponse.ok(s3Service.uplode(files, "image"));
    }

    // 배너 등록
    @PostMapping("/banner")
    public BaseResponse<List<ImageDto>> uploadBanner(@RequestBody List<MultipartFile> files) {
        return BaseResponse.ok(s3Service.uplode(files, "banner"));
    }

    // 전체 배너 조회
    @PostMapping("/banner")
    public BaseResponse<List<ImageDto>> getBanners() {
        return BaseResponse.ok(s3Service.getBanners());
    }
}
