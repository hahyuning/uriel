package com.uriel.travel.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.PackageRequestDto;
import com.uriel.travel.dto.PackageResponseDto;
import com.uriel.travel.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;
    ObjectMapper snakeMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    // 패키지 등록
    @PostMapping("/create")
    public BaseResponse<Void> create(@RequestBody PackageRequestDto.Create requestDto) {
        packageService.create(requestDto);
        return BaseResponse.ok();
    }

    // 패키지 수정
    @PutMapping("/{packageId}/update")
    public BaseResponse<Void> modify(@RequestBody PackageRequestDto.Update requestDto,
                                     @PathVariable Long packageId) {
        packageService.update(requestDto, packageId);
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
