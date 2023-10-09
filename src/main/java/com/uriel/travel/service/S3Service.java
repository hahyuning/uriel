package com.uriel.travel.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.uriel.travel.domain.Banner;
import com.uriel.travel.domain.Package;
import com.uriel.travel.domain.Thumbnail;
import com.uriel.travel.dto.ImageDto;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.BannerRepository;
import com.uriel.travel.repository.PackageRepository;
import com.uriel.travel.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final BannerRepository bannerRepository;
    private final PackageRepository packageRepository;
    private final ThumbnailRepository thumbnailRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 배너 업로드
    public List<ImageDto> uploadBanners(List<MultipartFile> files) {
        List<ImageDto> banners = upload(files, "banners");

        // 배너 저장
        banners.forEach(banner -> {
            bannerRepository.save(new Banner(banner.getOriginalImageName(),
                    banner.getUploadImageName(),
                    banner.getImagePath(),
                    banner.getImageUrl()));
        });

        return banners;
    }

    // 썸네일 업로드
    public void uploadThumbnails(List<MultipartFile> files, Long id) {
        List<ImageDto> dtoList = upload(files, "thumbnails");

        Package aPackage = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        dtoList.forEach(dto -> {
            Thumbnail thumbnail = new Thumbnail(dto.getOriginalImageName(),
                    dto.getUploadImageName(),
                    dto.getImagePath(),
                    dto.getImageUrl());

            thumbnailRepository.save(thumbnail);
            thumbnail.setPackage(aPackage);
        });
    }

    // 다중 이미지 업로드
    public List<ImageDto> upload(List<MultipartFile> files, String type) {

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
    @Transactional(readOnly = true)
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

    // 파일 1개 업로드
    public ImageDto upload(MultipartFile file) {

        String imagePath = "image/" + getFoldername();

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

        return ImageDto.builder()
                    .originalImageName(originalImageName)
                    .uploadImageName(uploadImageName)
                    .imagePath(imagePath)
                    .imageUrl(imageUrl).build();

    }
}
