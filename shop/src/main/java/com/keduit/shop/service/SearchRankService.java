package com.keduit.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keduit.shop.repository.SearchRankRepository;
import com.keduit.shop.entity.SearchRank;
import java.util.List;

@Service
public class SearchRankService {

    @Autowired
    private SearchRankRepository searchRankRepository;

    public List<SearchRank> getAllSearchRank() {
        return searchRankRepository.findAll();
    }
}
