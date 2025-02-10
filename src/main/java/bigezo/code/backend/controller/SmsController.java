package bigezo.code.backend.controller;

import bigezo.code.backend.service.SendSms;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {
    private final SendSms sendSms = new SendSms();

    @PostMapping("/send")
    public String sendSms(@RequestParam String phone, @RequestParam String message) {
        sendSms.sendSms(message, phone);
        return "SMS sent successfully";
    }
}


