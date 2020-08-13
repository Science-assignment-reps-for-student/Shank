package kr.hs.dsm_scarfs.shank.service.email;

import lombok.RequiredArgsConstructor;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;
import net.sargue.mailgun.content.Body;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SesEmailServiceImpl implements EmailService{

    @Value("${spring.email.domain}")
    private String domain;

    @Value("${spring.email.apikey}")
    private String apiKey;

    @Override
    public void sendEmail(String receiveEmail, String code) {
        Configuration configuration = new Configuration()
                .domain(domain)
                .apiKey(apiKey)
                .from("Avocat", "avocat@dsm.hs.kr");

        Body builder = new Body("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"><body><p style=\"color: red\">Test</p></body></html>", "");

        Response response = Mail.using(configuration)
                .to(receiveEmail)
                .subject("아보캣 인증 메일")
                .content(builder)
                .build()
                .send();


    }

}
