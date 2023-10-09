package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.PackageRequestDto;
import com.uriel.travel.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;

    // 패키지 등록
    @PostMapping("/create")
    public BaseResponse<Void> create(@RequestBody PackageRequestDto.Create requestDto) {
        packageService.create(requestDto);
        return BaseResponse.ok();
    }


    // 패키지 수정
    @PutMapping("/{packageId}/modify")
    public BaseResponse<Void> modify(@RequestBody PackageRequestDto.Update requestDto) {
        packageService.update(requestDto);
        return BaseResponse.ok();
    }


    // 패키지 삭제
    @PostMapping("/batch-delete")
    public BaseResponse<Void> delete(@RequestBody List<Long> packageIds) {
        packageService.delete(packageIds);
        return BaseResponse.ok();
    }
}
