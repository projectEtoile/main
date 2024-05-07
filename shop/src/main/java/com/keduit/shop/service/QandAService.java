package com.keduit.shop.service;
import com.keduit.shop.entity.QandA;
import com.keduit.shop.repository.QandARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
@RequiredArgsConstructor
public class QandAService {

    private final QandARepository qandARepository;

    public List<QandA> findQuestionsByItemId(Long itemId) {
        return qandARepository.findAllByItemId(itemId);
    }

    public List<QandA> getAllQuestions() {
        return qandARepository.findAll();
    }

    public void save(QandA qanda) {
        qandARepository.save(qanda);
    }

    // QandAService.java
    public List<QandA> findQuestionsByUserId(Long userId) {
        return qandARepository.findAllByMemberId(userId);
    }

    public Page<QandA> findQuestionsByUserId(Long userId, Pageable pageable) {
        return qandARepository.findAllByMemberId(userId, pageable);
    }

}
