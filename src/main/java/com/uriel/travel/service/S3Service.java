package com.uriel.travel.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.uriel.travel.domain.Banner;
import com.uriel.travel.dto.ImageDto;
import com.uriel.travel.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final BannerRepository bannerRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 업로드
    public List<ImageDto> uplode(List<MultipartFile> files, String type) {

        List<ImageDto> imageList = new ArrayList<>();
        String imagePath = type + "/" + getFoldername();

        files.forEach(file -> {
            String originalImageName = file.getOriginalFilename();
            String ext = originalImageName.substring(originalImageName.indexOf(".") + 1); // 확장자
            String uploadImageName = UUID.randomUUID().toString() + "." + ext;

            String imageUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                String keyName = imagePath + "/" + uploadImageName;

                amazonS3Client.putObject(
                        new PutObjectRequest(bucket, keyName, inputStream, objectMetadata));

                imageUrl = amazonS3Client.getUrl(bucket, keyName).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 배너 저장
            if (type.equals("banner")) {
                Banner banner = new Banner(originalImageName, uploadImageName, imagePath, imageUrl);
                bannerRepository.save(banner);
            }

            imageList.add(
                    ImageDto.builder()
                            .originalImageName(originalImageName)
                            .uploadImageName(uploadImageName)
                            .imagePath(imagePath)
                            .imageUrl(imageUrl).build()
            );

        });

        return imageList;
    }

    public String getFoldername() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", "/");
    }

    // 배너 목록 전체 조회
    public List<ImageDto> getBanners() {
        List<Banner> allBanners = bannerRepository.findAll();
        List<ImageDto> imageList = new ArrayList<>();

        allBanners.forEach(banner -> {
            imageList.add(ImageDto.builder()
                    .originalImageName(banner.getOriginalImageName())
                    .uploadImageName(banner.getUploadImageName())
                    .imagePath(banner.getImagePath())
                    .imageUrl(banner.getImageUrl())
                    .build());

        });

        return imageList;
    }
}
