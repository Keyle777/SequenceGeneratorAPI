package keyle.fun.SequenceGeneratorAPI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class SequenceGeneratorApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDatabaseSequenceServiceImpl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sequence/databaseSequenceServiceImpl"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void testRedisSequenceServiceImpl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sequence/redisSequenceServiceImpl"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void testSnowflakeSequenceServiceImpl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sequence/snowflakeSequenceServiceImpl"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"));
    }
}
