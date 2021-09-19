package io.ascopes.springbootjdk17;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class IntegrationTest {
  @Autowired
  MockMvc mvc;

  static Stream<String> names() {
    return Stream.of("Ashley", "Steve", "Jeff");
  }

  @ParameterizedTest(name = "Greeting {arguments} should return the expected response")
  @MethodSource("names")
  void test_greeting(String name) throws Exception {
    mvc.perform(get("/greetings/{name}", name))
        .andExpect(status().is(200))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Hello, %s!".formatted(name)));
  }
}
