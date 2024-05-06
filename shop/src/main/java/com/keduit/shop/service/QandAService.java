package com.keduit.shop.service;

import com.keduit.shop.entity.QandA;
import com.keduit.shop.repository.QandARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QandAService {

    private final QandARepository qandARepository;

    public List<QandA> findQuestionsByItemId(Long itemId) {
        return qandARepository.findAllByItemId(itemId);
    }

    public void save(QandA qanda) {
        qandARepository.save(qanda);
    }
}

