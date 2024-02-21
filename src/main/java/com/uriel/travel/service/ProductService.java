package com.uriel.travel.service;

import com.uriel.travel.domain.*;
import com.uriel.travel.domain.Package;
import com.uriel.travel.dto.ImageDto;
import com.uriel.travel.dto.product.PackageResponseDto;
import com.uriel.travel.dto.product.ProductDetailResponseDto;
import com.uriel.travel.dto.product.ProductRequestDto;
import com.uriel.travel.dto.filterCond.ProductFilter;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final PackageRepository packageRepository;
    private final ProductDetailRepository productDetailRepository;
    private final EntityManager entityManager;
    private final ProductRepositoryCustomImpl productRepositoryCustom;
    private final ThumbnailRepository thumbnailRepository;
    private final ScheduleRepository scheduleRepository;

    // 상품 등록
    public Long create(ProductRequestDto.Create requestDto) {
        Product product = requestDto.toEntity();

        // TODO: 상품코드 중복 검사 및 코드 부여


        // 패키지 연관관계
        Package packageById = packageRepository.findById(requestDto.getPackageId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        product.setPackage(packageById);

        // 상품 저장
        product.setPrivacy(requestDto.getPrivacy());
        Product savedProduct = productRepository.save(product);

        // 상품 디테일 등록
        ProductDetail productDetail = new ProductDetail(requestDto, savedProduct);
        productDetailRepository.save(productDetail);

        return savedProduct.getId();
    }

    // 상품 임시저장
    public void temporarySave(ProductRequestDto.Create requestDto) {
        Product product = requestDto.toEntity();

        // 패키지 연관관계
        Package packageById = packageRepository.findById(requestDto.getPackageId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        product.setPackage(packageById);

        Product savedProduct = productRepository.save(product);

        // 상품 디테일 등록
        ProductDetail productDetail = new ProductDetail(requestDto, savedProduct);
        productDetailRepository.save(productDetail);
    }

    // 상품 임시저장 수정
    public void temporaryUpdate(ProductRequestDto.Update requestDto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        // 패키지 수정
        if (!Objects.equals(product.getAPackage().getId(), requestDto.getPackageId())) {
            // 기존 패키지에서 삭제
            Package oldPackage = packageRepository.findById(product.getAPackage().getId())
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            oldPackage.getProductList().remove(product);

            // 새로운 패키지에 등록
            Package newPackage = packageRepository.findById(requestDto.getPackageId())
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            product.setPackage(newPackage);
        }
        product.update(requestDto);
        product.setPrivacy(Release.TEMPORARY.getViewName());

        ProductDetail productDetail = productDetailRepository.findByProductId(productId);
        productDetail.update(requestDto);
    }

    // 상품 수정
    public void update(ProductRequestDto.Update requestDto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        // 패키지 수정
        if (!Objects.equals(product.getAPackage().getId(), requestDto.getPackageId())) {
            // 기존 패키지에서 삭제
            Package oldPackage = packageRepository.findById(product.getAPackage().getId())
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            oldPackage.getProductList().remove(product);

            // 새로운 패키지에 등록
            Package newPackage = packageRepository.findById(requestDto.getPackageId())
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            product.setPackage(newPackage);
        }
        product.update(requestDto);

        ProductDetail productDetail = productDetailRepository.findByProductId(productId);
        productDetail.update(requestDto);
    }

    // 상품 일괄 삭제
    public void delete(List<Long> ids) {
        ids.forEach(productId -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            Package aPackage = packageRepository.findById(product.getAPackage().getId())
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            ProductDetail productDetail = productDetailRepository.findByProductId(productId);
            productDetailRepository.delete(productDetail);

            aPackage.getProductList().remove(product);
            productRepository.delete(product);
        });
    }

    // 상품 공개/비공개 처리
    public void privacyUpdate(String privacy, List<Long> ids) {
        ids.forEach(id -> {
            Product product = productRepository.findById(id)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            product.setPrivacy(privacy);
        });
    }

    // 상품 복사
    public void duplicate(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        entityManager.detach(product);
        product.idInitialize();
        product.setPrivacy("임시저장");
        productRepository.save(product);

        ProductDetail productDetail = productDetailRepository.findByProductId(productId);
        entityManager.detach(productDetail);
        productDetail.idInitialize();
        productDetail.setProduct(product);
        productDetailRepository.save(productDetail);
    }

    // 패키지별 상품 조회
    public Page<ProductFilter.ProductFilterResponseDto> searchByPackage(ProductFilter.ProductFilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), filterCond.getLimit());
        return productRepositoryCustom.searchByPackage(filterCond, pageRequest);
    }

    // 상품 상세 조회
    public ProductDetailResponseDto productDetail(Long productId) {
        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        // 상품 디테일 조회
        ProductDetail productDetail = productDetailRepository.findByProductId(productId);

        ProductDetailResponseDto responseDto = new ProductDetailResponseDto();
        responseDto.setProductInfo(ProductDetailResponseDto.GetProduct.of(product, productDetail));

        // 패키지 조회
        Package aPackage = packageRepository.findById(product.getAPackage().getId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        PackageResponseDto.PackageInfo packageInfo = PackageResponseDto.PackageInfo.of(aPackage, "detail");

        // 썸네일
        List<Thumbnail> thumbnails = thumbnailRepository.findAllByPackageId(aPackage.getId());
        List<ImageDto> thumbnailList = new ArrayList<>();
        thumbnails.forEach(thumbnail ->
                thumbnailList.add(ImageDto.builder()
                    .originalImageName(thumbnail.getOriginalImageName())
                    .uploadImageName(thumbnail.getUploadImageName())
                    .imagePath(thumbnail.getImagePath())
                    .imageUrl(thumbnail.getImageUrl()).build()));
        packageInfo.setThumbnailList(thumbnailList);

        // 일정
        List<Schedule> schedules = scheduleRepository.findAllByPackageId(aPackage.getId());
        List<PackageResponseDto.ScheduleResponseDto> scheduleList = new ArrayList<>();
        schedules.forEach(schedule ->
                PackageResponseDto.ScheduleResponseDto.of(schedule, "detail"));

        packageInfo.setScheduleList(scheduleList);
        responseDto.setPackageInfo(packageInfo);

        return responseDto;
    }

}
