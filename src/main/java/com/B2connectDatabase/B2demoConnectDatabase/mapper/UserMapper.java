package com.B2connectDatabase.B2demoConnectDatabase.mapper;

import com.B2connectDatabase.B2demoConnectDatabase.dto.request.UserCreateRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.request.UserUpdateRequest;
import com.B2connectDatabase.B2demoConnectDatabase.dto.response.UserResponse;
import com.B2connectDatabase.B2demoConnectDatabase.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequest request);
//    Mapping tuwf request sang user
    // Mapp firstName thành lastName
//    @Mapping(source = "firstName", target = "lastName")
    // không map firstName
    @Mapping(target = "firstName", ignore = true)
    UserResponse toUserResponse (User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
