package com.B2connectDatabase.B2demoConnectDatabase.controller;

import com.B2connectDatabase.B2demoConnectDatabase.dto.request.ApiResponse;
import com.B2connectDatabase.B2demoConnectDatabase.dto.request.UserCreateRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.request.UserUpdateRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.response.UserResponse;
import com.B2connectDatabase.B2demoConnectDatabase.entity.User;
import com.B2connectDatabase.B2demoConnectDatabase.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreateRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    // lấy danh sách user
    @GetMapping
    List<User> getUser(){
        return userService.getUsers();
    }

    @GetMapping("/{Userid}")
    UserResponse getUser(@PathVariable("Userid") String Userid){
        return userService.getUser(Userid);
    }

    @PutMapping("/{Userid}")
    UserResponse updateUser(@PathVariable("Userid") String Userid, @RequestBody UserUpdateRequest request){
        return userService.updateUser(Userid, request);
    }

    @DeleteMapping("/{Userid}")
    String deleteUser(@PathVariable String Userid){
        userService.DeleteUser(Userid);
        return "User has been Delete";
    }
}


