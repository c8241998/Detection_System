package com.czdsb.demo.service;

import com.czdsb.demo.model.Camera;
import com.czdsb.demo.model.User;

import java.util.List;

public interface UserService {

    void createUser(User user);

    int deleteByUsername(String username);

    int deleteByUserId(Integer userId);

    int modifyPassword(User user);

    int modifyName(User user);

    int modifyPhone(User user);

    User findByUserId(Integer userId);

    User findByUsername(String username);

    User findByPhone(String phone);

    User findByCamera(Camera camera);

    User findByCamera(String cameraId);

    void bindWithCamera(User user, Camera camera);
}