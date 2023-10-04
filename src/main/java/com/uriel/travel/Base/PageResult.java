package com.uriel.travel.Base;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResult <T> {
    int currentPage; //현재 페이지 번호
    int pageSize; //한 페이지 당 원소의 개수
    int totalPage; //전체 페이지 수
    Long totalElements; // 모든 페이지에 존재하는 총 원소 수
    List<T> dataList; //데이터
    public PageResult(Page<T> data){
        currentPage=data.getPageable().getPageNumber();
        pageSize=data.getPageable().getPageSize();
        totalPage=data.getTotalPages();
        totalElements=data.getTotalElements();
    }
    public static <T>PageResult<T> ok(Page<T> data){
        return new PageResult<>(data);
    }


}
