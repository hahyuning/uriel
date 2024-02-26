package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.community.ImageDto;
import com.uriel.travel.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final S3Service s3Service;

    // 이미지 한개 등록 (ckeditor 사진 등록 시 사용)
    @PostMapping
    public BaseResponse<ImageDto> upload(@RequestBody MultipartFile file) {
        return BaseResponse.ok(s3Service.upload(file));
    }

    // 이미지 한개 삭제
    @DeleteMapping
    public BaseResponse<Void> delete(@RequestBody MultipartFile file) {
        return BaseResponse.ok();
    }

    // 다중 이미지 등록
    @PostMapping("/multiple-upload")
    public BaseResponse<List<ImageDto>> multipleUpload(@RequestBody List<MultipartFile> files) {
        return BaseResponse.ok(s3Service.upload(files, "image"));
    }

    // 배너 등록
    @PostMapping("/banners")
    public BaseResponse<List<ImageDto>> uploadBanner(@RequestBody List<MultipartFile> files) {
        return BaseResponse.ok(s3Service.uploadBanners(files));
    }

    // 전체 배너 조회
    @GetMapping("/banners")
    public BaseResponse<List<ImageDto>> getBanners() {
        return BaseResponse.ok(s3Service.getBanners());
    }
}
