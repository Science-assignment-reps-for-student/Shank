package kr.hs.dsm_scarfs.shank.service.email;

import lombok.SneakyThrows;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;
import net.sargue.mailgun.content.Body;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${email.domain}")
    private String domain;

    @Value("${email.apikey}")
    private String apiKey;

    @Async
    @Override
    public void sendEmail(String receiveEmail, String code) {
        Configuration configuration = new Configuration()
                .domain(domain)
                .apiKey(apiKey)
                .from("Avocat", "avocat@dsm.hs.kr");

        Body builder = new Body(convertHtmlWithCode(code), "");

        Response response = Mail.using(configuration)
                .to(receiveEmail)
                .subject("Avocat 회원 인증 메일")
                .content(builder)
                .build()
                .send();

        if (!response.isOk())
            throw new RuntimeException();
    }

    @SneakyThrows
    private String convertHtmlWithCode(String code) {
        InputStream inputStream = new ClassPathResource("static/email.html").getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder stringBuilder = new StringBuilder();
        reader.lines()
                .filter(Objects::nonNull)
                .forEach(stringBuilder::append);

        return stringBuilder.toString().replace("{%code%}", code);
    }

}
