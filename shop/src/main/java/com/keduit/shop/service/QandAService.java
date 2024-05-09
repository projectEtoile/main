package com.keduit.shop.service;

import com.keduit.shop.dto.AdminMemberSearchDTO;
import com.keduit.shop.dto.AdminQNASearchDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.QandA;
import com.keduit.shop.repository.QandARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QandAService {

    private final QandARepository qandARepository;

    public List<QandA> findQuestionsByItemId(Long itemId) {
        return qandARepository.findAllByItemId(itemId);
    }
    public Page<QandA> findQuestionsByItemId(Long itemId, Pageable pageable) {
        return qandARepository.findAllByItemId(itemId, pageable);
    }

    public List<QandA> getAllQuestions() {
        return qandARepository.findAll();
    }

    public void save(QandA qanda) {
        qandARepository.save(qanda);
    }

    public Page<QandA> getAdminQandAListPage(AdminQNASearchDTO adminQNASearchDTO, Pageable pageable){
        return qandARepository.getAdminQandAListPage(adminQNASearchDTO,pageable);
    }

    // QandAService.java
    public List<QandA> findQuestionsByUserId(Long userId) {
        return qandARepository.findAllByMemberId(userId);
    }

    public Page<QandA> findQuestionsByUserId(Long userId, Pageable pageable) {
        return qandARepository.findAllByMemberId(userId, pageable);
    }

}
