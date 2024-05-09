package com.keduit.shop.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.keduit.shop.dto.AdminMemberSearchDTO;
import com.keduit.shop.dto.AdminQNASearchDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.QandA;
import com.keduit.shop.repository.QandARepository;
import com.keduit.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.lang.module.FindException;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class AdminMemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final QandARepository qandARepository;

    @GetMapping({"/members/{page}", "/members"})
    public String memberMangeListPage(Model model,
                                      @PathVariable("page") Optional<Integer> page, // 유저에게 받는 page 숫자.
                                      AdminMemberSearchDTO adminMemberSearchDTO) { //쿼리문을 날리기 위한 정보들!

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Member> members = memberService.getAdminMemberPage(adminMemberSearchDTO, pageable);

        System.out.println("----- members.getContent() : " + members.getContent());
        System.out.println("----- adminMemberSearchDTO: " + adminMemberSearchDTO);

        model.addAttribute("members", members);
        model.addAttribute("adminMemberSearchDTO", adminMemberSearchDTO);
        model.addAttribute("maxPage", 10);

        System.out.println(members.getNumber()+"@@@@@@@@@@@@@@@@@@@@@@");

        return "admin/memberMng";
    }

    @GetMapping({"/qNaLists/{page}", "/qNaLists"})
    public String qNaList(Model model,
                                      @PathVariable("page") Optional<Integer> page, // 유저에게 받는 page 숫자.
                                      AdminQNASearchDTO adminQNASearchDTO) { //쿼리문을 날리기 위한 정보들!

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<QandA> qNaList = qandARepository.getAdminQandAListPage(adminQNASearchDTO,pageable);

        model.addAttribute("qNaList", qNaList);
        model.addAttribute("adminQNASearchDTO", adminQNASearchDTO);
        model.addAttribute("maxPage", 10);

        return "admin/qNaList";
    }

    @GetMapping("/qNa")
    public @ResponseBody ResponseEntity getQna(@RequestParam("id") Long id) throws JsonProcessingException {

        QandA qandA = qandARepository.findById(id).orElseThrow(EntityNotFoundException::new);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String jsonValue;
        jsonValue = objectMapper.writeValueAsString(qandA);

        System.out.println(jsonValue);
        return new ResponseEntity(jsonValue, HttpStatus.OK);
    }

    @PutMapping("/qNa")
    public ResponseEntity<String> getAnswer(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data1 = objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});

        System.out.println(data1.get("id"));
        System.out.println("@@@@@@@@@@@@");

        Long id = Long.parseLong(String.valueOf(data1.get("id")));

        QandA qandA = qandARepository.findById(id).orElseThrow(EntityNotFoundException::new);

        qandA.setAnswer((String) data1.get("answer"));

        qandARepository.save(qandA);

        System.out.println("컨트롤러 성공적");
        return new ResponseEntity("답변이 성공적으로 작성됬습니다", HttpStatus.OK);
    }


}
