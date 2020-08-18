package kr.hs.dsm_scarfs.shank.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.dsm_scarfs.shank.ShankApplication;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerification;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationStatus;
import kr.hs.dsm_scarfs.shank.payload.request.AccountRequest;
import kr.hs.dsm_scarfs.shank.payload.request.VerifyCodeRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration(classes = ShankApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "local"})
class UserApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        emailVerificationRepository.save(
                EmailVerification.builder()
                    .email("machiro119@naver.com")
                    .code("ABCDE")
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
        VerifyCodeRequest request = new VerifyCodeRequest("machiro119@naver.com", "ABCDE");
        requestMvc(put("/user/email/verify"), request);
    }

    @Test
    public void signUpTest() throws Exception {
        emailVerifyTest();
        AccountRequest request = new AccountRequest("machiro119@naver.com", "P@ssw0rd");
        requestMvc(post("/user"), request);
    }

    private void requestMvc(MockHttpServletRequestBuilder methode, Object obj) throws Exception {
        String baseUrl = "http://localhost:" + port;

        mvc.perform(methode
                .content(new ObjectMapper()
                        .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
