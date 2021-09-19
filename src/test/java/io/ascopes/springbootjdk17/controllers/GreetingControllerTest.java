package io.ascopes.springbootjdk17.controllers;

import io.ascopes.springbootjdk17.models.GreetingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GreetingController tests")
class GreetingControllerTest {
  GreetingController greetingController;

  @BeforeEach
  void setUp() {
    this.greetingController = new GreetingController();
  }

  @DisplayName("Test that the greetMe method behaves as expected")
  @Test
  void test_expected_greeting_given() {
    GreetingDto response = greetingController.greetMe("ashley");

    assertThat(response.message()).isEqualTo("Hello, ashley!");
  }
}
