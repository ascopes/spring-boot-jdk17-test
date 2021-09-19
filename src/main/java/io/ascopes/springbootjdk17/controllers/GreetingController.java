package io.ascopes.springbootjdk17.controllers;

import io.ascopes.springbootjdk17.models.GreetingDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/greetings")
@RestController
public class GreetingController {
  @GetMapping("/{name}")
  public GreetingDto greetMe(@PathVariable String name) {
    return new GreetingDto("Hello, " + name + "!");
  }
}
