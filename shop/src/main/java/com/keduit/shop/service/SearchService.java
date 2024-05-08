package com.keduit.shop.service;

import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.dto.SearchDTO;
import com.keduit.shop.page.Pagination;
import com.keduit.shop.page.PagingResponse;
import com.keduit.shop.repository.SearchRepository;

import java.util.Collections;
import java.util.List;

public class SearchService {

  SearchRepository searchRepository;
  /**
   * 리스트 조회
   * @param params - search conditions
   * @return 리스트
   */
  public PagingResponse<ItemFormDTO> findAllPost(final SearchDTO params) {

    // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null을 담아 반환
    int count = searchRepository.count(params);
    if (count < 1) {
      return new PagingResponse<>(Collections.emptyList(), null);
    }

    // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
    Pagination pagination = new Pagination(count, params);
    params.setPagination(pagination);

// 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
    List<ItemFormDTO> list = searchRepository.findAll(params);
    return new PagingResponse<>(list, pagination);
  }
}
