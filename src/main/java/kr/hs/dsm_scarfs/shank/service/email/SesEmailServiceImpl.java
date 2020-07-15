package kr.hs.dsm_scarfs.shank.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SesEmailServiceImpl implements EmailService{

    private final AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync;

    private static final String UTF_8_ENCODED_SOURCE_NAME = "=?utf-8?B?U0NBUkZT?=";

    @Override
    public void sendEmail(String receiveEmail, String code) {

        SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
                .withDestination(new Destination().withToAddresses(receiveEmail))
                .withTemplate("ScarfsEmailConfirmTemplate")
                .withSource(UTF_8_ENCODED_SOURCE_NAME + "<?>")
                .withTemplateData("{\"code\": \"" + code + "\", \"email\": \"" + receiveEmail + "\"}");

        amazonSimpleEmailServiceAsync.sendTemplatedEmailAsync(request);
    }

}
