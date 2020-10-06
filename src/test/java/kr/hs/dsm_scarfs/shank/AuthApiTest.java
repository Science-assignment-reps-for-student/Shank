package kr.hs.dsm_scarfs.shank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.payload.request.AccountRequest;
import kr.hs.dsm_scarfs.shank.payload.response.TokenResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "local"})
class AuthApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        studentRepository.save(
                Student.builder()
                    .email("machiro119@naver.com")
                    .password(passwordEncoder.encode("P@ssw0rd"))
                    .name("이대성")
                    .studentNumber("1212")
                    .build()
        );
    }

    @AfterEach
    public void clean() {
        studentRepository.deleteAll();
    }

    @Test
    public void signInTest() throws Exception {
        signIn();
    }

    @Test
    public void refreshToken() throws Exception {
        String url = "http://localhost:" + port;

        String content = signIn().getResponse().getContentAsString();
        TokenResponse response = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(content, TokenResponse.class);
        String refreshToken = response.getRefreshToken();

        mvc.perform(put(url + "/auth")
                .header("X-Refresh-Token", refreshToken))
                .andExpect(status().isOk());
    }

    private MvcResult signIn() throws Exception {
        String url = "http://localhost:" + port;

        AccountRequest accountRequest = new AccountRequest("machiro119@naver.com", "P@ssw0rd");

        return mvc.perform(post(url + "/auth")
                .content(new ObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }
}
