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

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QandAService {

    private final QandARepository qandARepository;

    public List<QandA> findQuestionsByItemId(Long itemId) {
        return qandARepository.findAllByItemId(itemId);
    }

    public void save(QandA qanda) {
        qandARepository.save(qanda);
    }

    public Page<QandA> getAdminQandAListPage(AdminQNASearchDTO adminQNASearchDTO, Pageable pageable){
        return qandARepository.getAdminQandAListPage(adminQNASearchDTO,pageable);
    }

}

