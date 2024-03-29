package com.uriel.travel.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.uriel.travel.domain.dto.ImageDto;
import com.uriel.travel.domain.entity.Banner;
import com.uriel.travel.domain.entity.Package;
import com.uriel.travel.domain.entity.Thumbnail;
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

        // 이전 배너 삭제
        List<Banner> allBanners = bannerRepository.findAll();
        allBanners.forEach( banner -> {
                delete(banner.getImagePath(), banner.getUploadImageName());
                bannerRepository.delete(banner);
            }
        );

        // 배너 저장
        banners.forEach(banner -> {
            bannerRepository.save(new Banner(banner.getOriginalImageName(),
                    banner.getUploadImageName(),
                    banner.getImagePath(),
                    banner.getImageUrl()));
        });

        return banners;
    }

    // s3 이미지 삭제
    public void delete(String imagePath, String uploadImageName) {
        try {
            String keyName = imagePath + "/" + uploadImageName;
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, keyName);

            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket, keyName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        allBanners.forEach(banner -> imageList.add(ImageDto.builder()
                .originalImageName(banner.getOriginalImageName())
                .uploadImageName(banner.getUploadImageName())
                .imagePath(banner.getImagePath())
                .imageUrl(banner.getImageUrl())
                .build()));
        return imageList;
    }

    // 파일 1개 업로드 (에디터)
    public ImageDto upload(MultipartFile file) {

        String imagePath = "images/" + getFoldername();

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

    // 썸네일 업로드
    public void uploadThumbnails(List<MultipartFile> files, Long id) {
        List<ImageDto> dtoList = upload(files, "thumbnails");

        Package aPackage = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_PACKAGE));

        dtoList.forEach(dto -> {
            Thumbnail thumbnail = new Thumbnail(dto.getOriginalImageName(),
                    dto.getUploadImageName(),
                    dto.getImagePath(),
                    dto.getImageUrl());

            thumbnailRepository.save(thumbnail);
            thumbnail.setPackage(aPackage);
        });
    }

    // 기존 썸네일 삭제
    public void deleteThumbnail(Long id) {
        List<Thumbnail> thumbnailList = thumbnailRepository.findAllByPackageId(id);
        Package aPackage = packageRepository.findById(id)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        thumbnailList.forEach(thumbnail -> {
            delete(thumbnail.getImagePath(), thumbnail.getUploadImageName());
            aPackage.getThumbnailList().remove(thumbnail);
        });
    }

    // 썸네일 복사
    public Thumbnail duplicateThumbnail(Thumbnail thumbnail) {

        String imagePath = "thumbnails/" + getFoldername();
        String ext = thumbnail.getOriginalImageName().substring(thumbnail.getOriginalImageName().indexOf(".") + 1); // 확장자
        String newUploadImageName = UUID.randomUUID().toString() + "." + ext;

        String originalKey = thumbnail.getImagePath() + "/" + thumbnail.getUploadImageName();
        String newKey = imagePath + "/" + newUploadImageName;

        try {
            //Copy 객체 생성
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(
                    bucket,
                    originalKey,
                    bucket,
                    newKey
            );
            //Copy
            this.amazonS3Client.copyObject(copyObjRequest);

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return Thumbnail.builder()
                .originalImageName(thumbnail.getOriginalImageName())
                .uploadImageName(newUploadImageName)
                .imagePath(imagePath)
                .imageUrl(amazonS3Client.getUrl(bucket, newKey).toString()).build();
    }
}
