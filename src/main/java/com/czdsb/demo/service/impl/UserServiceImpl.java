package com.czdsb.demo.service.impl;

import com.czdsb.demo.model.Camera;
import com.czdsb.demo.model.User;
import com.czdsb.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO user(username, password, name, phone) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getPhone());
    }

    @Override
    public int deleteByUsername(String username) {
        String sql = "DELETE FROM user WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    @Override
    public int deleteByUserId(Integer userId) {
        String sql = "DELETE FROM user WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    @Override
    public int modifyPassword(User user) {
        String sql = "UPDATE user SET password = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, user.getPassword(), user.getUserId());
    }

    @Override
    public int modifyName(User user) {
        String sql = "UPDATE user SET name = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getUserId());
    }

    @Override
    public int modifyPhone(User user) {
        String sql = "UPDATE user SET phone = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, user.getPhone(), user.getUserId());
    }

    @Override
    public User findByUserId(Integer userId) {
        String sql = "select * from user where userId = ?";
        return find(sql, new Object[]{userId});
    }

    @Override
    public User findByUsername(String username) {
        String sql = "select * from user where username = ?";
        return find(sql, new Object[]{username});
    }

    @Override
    public User findByPhone(String phone) {
        String sql = "select * from user where phone = ?";
        return find(sql, new Object[]{phone});
    }

    @Override
    public User findByCamera(Camera camera) {
        return findByCamera(camera.getCameraId());
    }

    @Override
    public User findByCamera(String cameraId) {
        String sql = "select userId from cameraToUser where cameraId = ?";
        return find(sql, new Object[]{cameraId});
    }

    @Override
    public void bindWithCamera(User user, Camera camera) {
        String sql = "INSERT INTO cameraToUser(userId, cameraId) VALUES(?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), camera.getCameraId());
    }

    private static RowMapper<User> userMapper = new BeanPropertyRowMapper<>(User.class);

    private User find(String sql, Object[] args) {
        try {
            return jdbcTemplate.queryForObject(sql, args, userMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}