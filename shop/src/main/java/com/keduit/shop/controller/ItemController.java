package com.keduit.shop.controller;

import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.dto.QandADTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.QandA;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.service.ItemService;
import com.keduit.shop.service.MemberService;
import com.keduit.shop.service.QandAService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final QandAService qandAService;

    @GetMapping("/categoryPadding")
    public String main() {
        return "category/categoryPadding";
    }


    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        try {
            ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
            model.addAttribute("item", itemFormDTO);

            // QandA 목록 가져오기
            List<QandA> qandaList = qandAService.findQuestionsByItemId(itemId);
            model.addAttribute("qandaList", qandaList);

            return "item/itemDtl";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            return "errorPage";
        }
    }


    // 컨트롤러 메서드에서 QandA 목록을 모델에 추가


    // Controller 클래스
    @PostMapping("/item/{itemId}/submitQuestion")
    public ResponseEntity<String> submitQuestion(@PathVariable("itemId") Long itemId, @RequestBody QandADTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Member member = memberService.findByEmail(userEmail);

        // 로그로 이메일 확인
        System.out.println("Authenticated User Email: " + userEmail);

        try {
            Item item = itemService.getItemById(itemId);
            QandA qanda = new QandA();
            qanda.setTitle(request.getTitle());
            qanda.setQuestion(request.getQuestion());
            qanda.setAnswer("");
            qanda.setMember(member);
            qanda.setItem(item);
            qanda.setEmail(userEmail);

            qandAService.save(qanda);

            return ResponseEntity.ok("질문이 제출되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("해당 아이템을 찾을 수 없습니다.");
        }
    }

    @GetMapping("/item/questions")
    public ResponseEntity<List<QandADTO>> getAllQuestions() {
        List<QandA> questions = qandAService.getAllQuestions();
        List<QandADTO> questionDTOs = questions.stream()
                .map(qanda -> new QandADTO(qanda.getTitle(),qanda.getQuestion(),qanda.getAnswer(),qanda.getEmail(),qanda.getId())) // QandA 엔티티를 QandADTO로 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionDTOs);
    }


    @GetMapping("/item/{itemId}/questions")
    public ResponseEntity<Map<String, Object>> getQuestionsByItemId(@PathVariable("itemId") Long itemId, Pageable pageable) {
        Page<QandA> page = qandAService.findQuestionsByItemId(itemId, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("questions", page.getContent().stream()
                .map(qanda -> new QandADTO(qanda.getTitle(), qanda.getQuestion(), qanda.getAnswer(), qanda.getEmail(), qanda.getId()))
                .collect(Collectors.toList()));
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());

        return ResponseEntity.ok(response);
    }


}


