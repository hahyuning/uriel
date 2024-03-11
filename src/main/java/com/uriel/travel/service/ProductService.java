package com.uriel.travel.service;

import com.uriel.travel.domain.ProductState;
import com.uriel.travel.domain.SaveState;
import com.uriel.travel.domain.dto.ImageDto;
import com.uriel.travel.domain.dto.product.ProductDetailResponseDto;
import com.uriel.travel.domain.dto.product.ProductFilter;
import com.uriel.travel.domain.dto.product.ProductRequestDto;
import com.uriel.travel.domain.dto.travelPackage.PackageResponseDto;
import com.uriel.travel.domain.entity.Package;
import com.uriel.travel.domain.entity.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final OrderRepository orderRepository;

    // 상품 등록
    public Long create(ProductRequestDto.Create requestDto) {
        Product product = requestDto.toEntity();

        // 패키지 연관관계
        Package packageById = packageRepository.findById(requestDto.getPackageId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        product.setPackage(packageById);

        // 상품 저장
        product.setPrivacy(requestDto.getPrivacy());
        product.setSaveState(SaveState.SAVED);
        Product savedProduct = productRepository.save(product);

        // 상품 디테일 등록
        ProductDetail productDetail = new ProductDetail(requestDto, savedProduct);
        productDetail.setProduct(savedProduct);
        productDetailRepository.save(productDetail);

        // 상품 코드 생성
        savedProduct.setProductCode();

        return savedProduct.getId();
    }

    // 상품 임시저장
    public void temporaryCreate(ProductRequestDto.Create requestDto) {
        Product product = requestDto.toEntity();

        // 패키지 연관관계
        Package packageById = packageRepository.findById(requestDto.getPackageId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        product.setPackage(packageById);
        product.setPrivacy(requestDto.getPrivacy());
        product.setSaveState(SaveState.TEMPORARY);
        Product savedProduct = productRepository.save(product);

        // 상품 디테일 등록
        ProductDetail productDetail = new ProductDetail(requestDto, savedProduct);
        productDetailRepository.save(productDetail);

        // 상품 코드 생성
        savedProduct.setProductCode();
    }

    // 상품 수정
    public void update(ProductRequestDto.Update requestDto, Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));


        // 패키지 수정
        if (!Objects.equals(product.getAPackage().getId(), requestDto.getPackageId())) {
            Package newPackage = updatePackage(product, requestDto.getPackageId());
            product.setPackage(newPackage);
        }

        ProductDetail productDetail = productDetailRepository.findByProductId(productId);

        int adultPrice = productDetail.getAdultPrice();
        int adultSurcharge = productDetail.getAdultSurcharge();
        int childPrice = productDetail.getChildPrice();
        int childSurcharge = productDetail.getChildSurcharge();
        int infantPrice = productDetail.getInfantPrice();
        int infantSurcharge = productDetail.getInfantSurcharge();

        int newAdultPrice = requestDto.getAdultPrice();
        int newAdultSurcharge = requestDto.getAdultSurcharge();
        int newChildPrice = requestDto.getChildPrice();
        int newChildSurcharge = requestDto.getChildSurcharge();
        int newInfantPrice = requestDto.getInfantPrice();
        int newInfantSurcharge = requestDto.getInfantSurcharge();

        // 출발일시가 변경된 경우
        if (!product.getStartDate().toLocalDate().equals(requestDto.getStartDate().toLocalDate())) {
            orderRepository.findByProductId(productId)
                .forEach(order -> {
                    int adultCount = order.getAdultCount();
                    int childCount = order.getChildCount();
                    int infantCount = order.getInfantCount();

                    int newAdultCount = 0;
                    int newChildCount = 0;
                    int newInfantCount = 0;

                    List<Traveler> travelerList = order.getTravelerList();

                    for (Traveler traveler : travelerList) {
                        LocalDate birth = traveler.getBirth();
                        int birthYear = birth.getYear();
                        int birthMonth = birth.getMonthValue();
                        int birthDay = birth.getDayOfMonth();

                        int startYear = requestDto.getStartDate().getYear();
                        int startMonth = requestDto.getStartDate().getMonthValue();
                        int startDay = requestDto.getStartDate().getDayOfMonth();

                        int age = startYear - birthYear;
                        if (birthMonth * 100 + birthDay > startMonth * 100 + startDay) {
                            age--;
                        }

                        if (age < 2) {
                            newInfantCount++;
                        } else if (age < 12) {
                            newChildCount++;
                        } else {
                            newAdultCount++;
                        }
                    }

                    if (adultCount != newAdultCount || childCount != newChildCount || infantCount != newInfantCount) {
                        order.updateTravelerCount(newAdultCount, newChildCount, newInfantCount);
                        Long newTotalPrice = ((long) (newAdultPrice + newAdultSurcharge) * newAdultCount + (long) (newChildPrice + newChildSurcharge) * newChildCount + (long) (newInfantPrice + newInfantSurcharge) * newInfantCount);
                        order.updateTotalPriceWithProductPriceChange(newTotalPrice);
                    }
                });
        }

        product.update(requestDto);

        // 상품 가격이 변동된 경우
        if (newAdultPrice != adultPrice || newAdultSurcharge != adultSurcharge || newChildPrice != childPrice ||
                newChildSurcharge != childSurcharge || newInfantPrice != infantPrice || newInfantSurcharge != infantSurcharge) {
            orderRepository.findByProductId(productId)
                    .forEach(order -> {
                        int adultCount = order.getAdultCount();
                        int childCount = order.getChildCount();
                        int infantCount = order.getInfantCount();

                        Long newTotalPrice = ((long) (newAdultPrice + newAdultSurcharge) * adultCount + (long) (newChildPrice + newChildSurcharge) * childCount + (long) (newInfantPrice + newInfantSurcharge) * infantCount);

                        order.updateTotalPriceWithProductPriceChange(newTotalPrice);
                    });
        }

        productDetail.update(requestDto);
    }

    // 상품 임시저장 -> 저장
    public void changeToSaveState(ProductRequestDto.Update requestDto, Long productId) {
        Product product = getProductByProductId(productId);

        // 패키지 수정
        if (!Objects.equals(product.getAPackage().getId(), requestDto.getPackageId())) {
            Package newPackage = updatePackage(product, requestDto.getPackageId());
            product.setPackage(newPackage);
        }

        product.update(requestDto);
        product.setSaveState(SaveState.SAVED);

        // 상품코드 부여
        product.setProductCode();

        ProductDetail productDetail = productDetailRepository.findByProductId(productId);
        productDetail.update(requestDto);
    }


    // 상품 일괄 삭제
    public void delete(List<Long> ids) {
        ids.forEach(productId -> {
            Product product = getProductByProductId(productId);

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

        Product product = getProductByProductId(productId);

        entityManager.detach(product);
        product.idInitialize();
        product.setSaveState(SaveState.TEMPORARY);
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

    public List<ProductFilter.ProductTwoMonthDate> getTowMothData(ProductFilter.ProductFilterCond filterCond) {
        filterCond.setStartDateTimeMin(LocalDateTime.now());
        filterCond.setStartDateTimeMax(LocalDateTime.now().plusMonths(2));

        return productRepositoryCustom.getTwoMonthProduct(filterCond);
    }

    // 상품 상세 조회
    public ProductDetailResponseDto productDetail(Long productId) {
        // 상품 조회
        Product product = getProductByProductId(productId);

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
                scheduleList.add(PackageResponseDto.ScheduleResponseDto.of(schedule, "detail")));

        packageInfo.setScheduleList(scheduleList);
        responseDto.setPackageInfo(packageInfo);

        return responseDto;
    }

    // 관리자용 상품 목록 조회
    public Page<ProductFilter.ProductFilterForAdminResponseDto> searchForAdmin(ProductFilter.ProductFilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), filterCond.getLimit());

        return productRepositoryCustom.searchForAdmin(filterCond, pageRequest);
    }

    // 아이디로 패키지 조회
    private Product getProductByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
    }

    // 패키지 정보 수정
    private Package updatePackage(Product product, Long packageId) {
        // 기존 패키지에서 삭제
        Package oldPackage = packageRepository.findById(product.getAPackage().getId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));

        oldPackage.getProductList().remove(product);

        return packageRepository.findById(packageId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
    }

    // 스케쥴링
    public void changeProductStateByScheduler() {
        LocalDate now = LocalDate.now();
        productRepository.findAll()
            .forEach(product -> {
                if (product.getStartDate().toLocalDate().isBefore(now.plusWeeks(2))) {
                    product.setProductState(ProductState.RESERVATION_DEADLINE);
                }
            });
    }
}
