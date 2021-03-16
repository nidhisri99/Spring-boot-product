package com.srinidhi.ProductList.Controller;


import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Controller
public class SendMailController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/mail")
    public String MailForm(){


        return "mail_form";
    }

//    @PostMapping("/mail")
    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public String SubmitMail(HttpServletRequest request, @RequestParam("attachment")MultipartFile multipartFile) throws MessagingException, UnsupportedEncodingException {
        String firstname = request.getParameter("firstname");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
        String send = request.getParameter("send");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);


        System.out.println(firstname+email+subject+body);

        String mailSubject =  firstname + "has sent u a message";
        String mailContents =  "<p><b>Sender name :<b> "+ firstname + "</p>";

        mailContents +=  "<p><b>Sender E_mail :</b> " + email + "</p>";
        mailContents +=  "<p><b>Subject  :</b> " + subject + "</p>";
        mailContents +=  "<p><b>Body :</b> " + body + "</p>";
        mailContents +=  "<hr><img src = 'cid:logo' />";

        helper.setFrom("gat@rakuten.com","Rakuten TEAM");
        helper.setTo(send);
        helper.setSubject(mailSubject);
        helper.setText(mailContents,true);

        ClassPathResource resource = new ClassPathResource("/static/rakuten.png");
        helper.addInline("logo",resource);

        if(!multipartFile.isEmpty()){
            String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            InputStreamSource source = new InputStreamSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return multipartFile.getInputStream();
                }
            };
            helper.addAttachment(filename,source);
        }

        mailSender.send(message);
        System.out.println("message successfully sent");
        return "message";
    }
}
