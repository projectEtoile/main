package com.keduit.shop.controller.admin;

import com.keduit.shop.dto.ItemFormDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class AdminOrderController {

    @GetMapping("/main")
    public String itemForm() {
        return "admin/main";
    }

}
