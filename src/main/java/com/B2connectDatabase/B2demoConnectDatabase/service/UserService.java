package com.B2connectDatabase.B2demoConnectDatabase.service;

import com.B2connectDatabase.B2demoConnectDatabase.dto.request.UserCreateRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.request.UserUpdateRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.response.UserResponse;
import com.B2connectDatabase.B2demoConnectDatabase.entity.User;
import com.B2connectDatabase.B2demoConnectDatabase.exception.AppException;
import com.B2connectDatabase.B2demoConnectDatabase.exception.ErrorCode;
import com.B2connectDatabase.B2demoConnectDatabase.mapper.UserMapper;
import com.B2connectDatabase.B2demoConnectDatabase.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User createUser(UserCreateRequest request){

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        // mã hóa pass
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request ){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        userMapper.updateUser(user,request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void DeleteUser(String id){
        userRepository.deleteById(id);
    }
}
