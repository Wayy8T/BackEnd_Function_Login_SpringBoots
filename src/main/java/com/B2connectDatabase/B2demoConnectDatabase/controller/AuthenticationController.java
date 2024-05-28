package com.B2connectDatabase.B2demoConnectDatabase.controller;

import com.B2connectDatabase.B2demoConnectDatabase.dto.request.ApiResponse;
import com.B2connectDatabase.B2demoConnectDatabase.dto.request.AuthenticationRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.request.IntrospectRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.response.AuthenticationResponse;
import com.B2connectDatabase.B2demoConnectDatabase.dto.response.IntrospectResponse;
import com.B2connectDatabase.B2demoConnectDatabase.entity.User;
import com.B2connectDatabase.B2demoConnectDatabase.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
