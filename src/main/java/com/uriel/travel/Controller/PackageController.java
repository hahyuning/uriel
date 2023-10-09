package com.uriel.travel.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.PackageRequestDto;
import com.uriel.travel.dto.PackageResponseDto;
import com.uriel.travel.service.PackageService;
import com.uriel.travel.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/packages")
@Slf4j
public class PackageController {

    private final PackageService packageService;
    private final S3Service s3Service;
    ObjectMapper snakeMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    // 패키지 등록
    @PostMapping("/create")
    public BaseResponse<Void> create(@RequestPart("data") PackageRequestDto.Create requestDto,
                                     @RequestPart("files") List<MultipartFile> files) {

        Long id = packageService.create(requestDto);
        s3Service.uploadThumbnails(files, id);
        return BaseResponse.ok();
    }

    // 패키지 수정
    @PutMapping("/{packageId}/update")
    public BaseResponse<Void> modify(@RequestPart("data") PackageRequestDto.Update requestDto,
                                     @RequestPart("files") List<MultipartFile> files,
                                     @PathVariable Long packageId) {
        packageService.update(requestDto, packageId);
        s3Service.deleteThumbnail(packageId);
        s3Service.uploadThumbnails(files, packageId);
        return BaseResponse.ok();
    }

    // 패키지 삭제
    @PostMapping("/batch-delete")
    public BaseResponse<Void> delete(@RequestBody Map<String, List<Long>> param) {
        List<Long> ids = param.get("ids");
        packageService.delete(ids);
        return BaseResponse.ok();
    }

    // 패키지 한건 조회
    @GetMapping("/{packageId}")
    public BaseResponse<PackageResponseDto.GetPackage> getPackageById(@PathVariable Long packageId) {
        return BaseResponse.ok(packageService.getPackageById(packageId));
    }
}
