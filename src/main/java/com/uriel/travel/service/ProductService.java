package com.uriel.travel.service;

import com.uriel.travel.domain.Package;
import com.uriel.travel.domain.Product;
import com.uriel.travel.domain.ProductDetail;
import com.uriel.travel.dto.ProductRequestDto;
import com.uriel.travel.dto.ProductResponseDto;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.PackageRepository;
import com.uriel.travel.repository.ProductDetailRepository;
import com.uriel.travel.repository.ProductRepository;
import com.uriel.travel.repository.ProductRepositoryCustomImpl;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 상품 등록
    public Long create(ProductRequestDto.Create requestDto) {
        Product product = requestDto.toEntity();

        // 상품코드

        // 패키지 연관관계
        Package packageById = packageRepository.findById(requestDto.getPackageId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        product.setPackage(packageById);

        // 상품 저장
        Product savedProduct = productRepository.save(product);

        // 상품 디테일 등록
        ProductDetail productDetail = new ProductDetail(requestDto, savedProduct);
        productDetailRepository.save(productDetail);

        return savedProduct.getId();
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

            aPackage.getProductList().remove(product);

            ProductDetail productDetail = productDetailRepository.findByProductId(productId);
            productDetailRepository.delete(productDetail);
            productRepository.delete(product);
        });
    }

    // 상품 일괄 복제
    public void duplicate(List<Long> ids) {
        ids.forEach(productId -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new CustomNotFoundException(ErrorCode.NOT_FOUND));

            entityManager.detach(product);
            product.idInitialize();
            productRepository.save(product);

            ProductDetail productDetail = productDetailRepository.findByProductId(productId);
            entityManager.detach(productDetail);
            productDetail.idInitialize();
            productDetail.setProduct(product);
            productDetailRepository.save(productDetail);
        });
    }

    // 상품 조회
    public Page<ProductResponseDto> searchByPackage(ProductRequestDto.FilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), filterCond.getLimit());
        return productRepositoryCustom.searchByFilterCond(filterCond, pageRequest);
    }
}
