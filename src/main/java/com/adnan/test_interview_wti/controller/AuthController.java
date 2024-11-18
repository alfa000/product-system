package com.adnan.test_interview_wti.controller;

import com.adnan.test_interview_wti.model.dto.LoginUserRequest;
import com.adnan.test_interview_wti.model.dto.RegisterUserRequest;
import com.adnan.test_interview_wti.model.dto.TokenResponse;
import com.adnan.test_interview_wti.model.dto.WebResponse;
import com.adnan.test_interview_wti.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/register")
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        authService.register(request);
        return WebResponse.<String>builder().message("Registration Success").data("OK").build();
    }

    @PostMapping(path = "/login")
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return WebResponse.<TokenResponse>builder().message("Login Success").data(tokenResponse).build();
    }
}
