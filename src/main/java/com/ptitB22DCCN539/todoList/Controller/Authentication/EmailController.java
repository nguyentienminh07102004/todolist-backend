package com.ptitB22DCCN539.todoList.Controller.Authentication;

import com.ptitB22DCCN539.todoList.Service.Email.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/${apiPrefix}/auth/email")
public class EmailController {
    private final IEmailService emailService;

    @PostMapping(value = "/send-mail")
    public void sendMail() {
        emailService.sendMailWithTemplate("nguyentienminh07102004@gmail.com", "CV xin việc", "cv");
    }
}
