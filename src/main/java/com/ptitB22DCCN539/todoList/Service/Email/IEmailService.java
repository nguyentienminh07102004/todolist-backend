package com.ptitB22DCCN539.todoList.Service.Email;

import java.util.Map;

public interface IEmailService {
    void sendMail(String to, String subject, String content);
    void sendMailWithHtml(String to, boolean isHtml, boolean isMultipart, String subject, String content);
    void sendMailWithTemplate(String to, String subject, String template, Map<String, Object> properties);
}
