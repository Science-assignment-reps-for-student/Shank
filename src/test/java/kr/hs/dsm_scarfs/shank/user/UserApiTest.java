package kr.hs.dsm_scarfs.shank.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.dsm_scarfs.shank.entites.authcode.AuthCode;
import kr.hs.dsm_scarfs.shank.entites.authcode.repository.AuthCodeRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerification;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationStatus;
import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import kr.hs.dsm_scarfs.shank.payload.request.VerifyCodeRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "local"})
class UserApiTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        authCodeRepository.save(new AuthCode("1111", "ABCDE"));

        emailVerificationRepository.save(
                EmailVerification.builder()
                    .email("machiro119@naver.com")
                    .code("이메일인증코드")
                    .status(EmailVerificationStatus.UNVERIFIED)
                    .build()
        );
    }

    @AfterEach
    public void clean() {
        emailVerificationRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    public void emailVerifyTest() throws Exception {
        VerifyCodeRequest request = new VerifyCodeRequest("machiro119@naver.com", "이메일인증코드");
        requestMvc(put("/user/email/verify"), request);
    }

    @Test
    public void signUpTest() throws Exception {
        emailVerifyTest();
        SignUpRequest request = new SignUpRequest(
                "machiro119@naver.com",
                "P@ssw0rd",
                "1111",
                "이대성",
                "ABCDE"
        );
        requestMvc(post("/user"), request);
    }

    private void requestMvc(MockHttpServletRequestBuilder methode, Object obj) throws Exception {
        mvc.perform(methode
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
