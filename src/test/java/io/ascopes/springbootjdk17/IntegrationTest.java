package io.ascopes.springbootjdk17;

import io.ascopes.springbootjdk17.config.SecurityConfig;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("App integration tests")
@SpringBootTest
class IntegrationTest {
  @Autowired
  MockMvc mvc;

  static Stream<String> names() {
    return Stream.of("Ashley", "Steve", "Jeff");
  }

  @DisplayName("Successful greeting requests")
  @ParameterizedTest(name = "for {arguments}")
  @MethodSource("names")
  @WithMockUser(
      username = "valid_user",
      password = "valid_password",
      roles = SecurityConfig.GREET_ROLE
  )
  void test_greeting(String name) throws Exception {
    mvc.perform(get("/greetings/{name}", name)
            .with(httpBasic("valid_user", "valid_password")))
        .andExpect(status().is(200))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Hello, %s!".formatted(name)));
  }

  @DisplayName("Greetings should not allow requests without authorization")
  @Test
  @WithMockUser
  void test_greeting_no_authorization() throws Exception {
    mvc.perform(get("/greetings/{name}", "unauthorized_user")
            .with(httpBasic("unauthorized_user", "unauthorized_password")))
        .andExpect(status().is(401));
  }

  @DisplayName("Greetings should not allow unknown users")
  @Test
  @WithMockUser(
      username = "valid_user",
      password = "valid_password",
      roles = SecurityConfig.GREET_ROLE
  )
  void test_greeting_no_such_user() throws Exception {
    mvc.perform(get("/greetings/{name}", "unauthorized_user")
            .with(httpBasic("unknown_user", "unknown_password")))
        .andExpect(status().is(401));
  }

  @DisplayName("Greetings should not allow users with incorrect credentials")
  @Test
  @WithMockUser(
      username = "valid_user",
      password = "valid_password",
      roles = SecurityConfig.GREET_ROLE
  )
  void test_greeting_incorrect_password() throws Exception {
    mvc.perform(get("/greetings/{name}", "unauthorized_user")
            .with(httpBasic("unknown_user", "unknown_password")))
        .andExpect(status().is(401));
  }

  @DisplayName("Greetings should not allow users with unexpected authorities to authorize")
  @Test
  @WithMockUser(
      username = "valid_user",
      password = "valid_password",
      roles = "forbidden_role"
  )
  void test_greeting_forbidden() throws Exception {
    mvc.perform(get("/greetings/{name}", "forbidden_user")
            .with(httpBasic("valid_user", "valid_password")))
        .andExpect(status().is(403));
  }
}
