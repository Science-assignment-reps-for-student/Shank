package kr.hs.dsm_scarfs.shank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hs.dsm_scarfs.shank.entites.board.Board;
import kr.hs.dsm_scarfs.shank.entites.board.repository.BoardRepository;
import kr.hs.dsm_scarfs.shank.entites.user.admin.Admin;
import kr.hs.dsm_scarfs.shank.entites.user.admin.repository.AdminRepository;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "local"})
class BoardApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        adminRepository.save(
                Admin.builder()
                    .id(1)
                    .email("test")
                    .name("홍길동")
                    .password(passwordEncoder.encode("P@ssw0rd"))
                    .build()
        );

        boardRepository.save(
                Board.builder()
                    .id(1)
                    .title("제목")
                    .content("내용")
                    .classNumber(1)
                    .adminId(1)
                    .view(0)
                    .build()
        );
    }


    @AfterEach
    public void clean() {
        adminRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "test", password = "P@ssw0rd")
    public void writeTest() throws Exception {
        mvc.perform(post("/board")
                .param("title", "제목")
                .param("content", "내용"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "P@ssw0rd")
    public void getBoardContentTest() throws Exception {
        mvc.perform(get("/board/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "P@ssw0rd")
    public void changeTest() throws Exception {
        mvc.perform(put("/board/1")
                .param("title", "바뀐 제목")
                .param("content", "바뀐 내용"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "P@ssw0rd")
    public void deleteTest() throws Exception {
        mvc.perform(delete("/board/1"))
                .andExpect(status().isOk());
    }

}
