package com.czdsb.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;

@Controller
public class MainController {

    @GetMapping({"/", "/index"})
    String templates(HttpServletRequest request) {
        return "index";
    }

    @GetMapping("/login")
    String login(HttpServletRequest request) {
        return "login";
    }

    @GetMapping("/signup")
    String signup(HttpServletRequest request) {
        return "signup";
    }

}