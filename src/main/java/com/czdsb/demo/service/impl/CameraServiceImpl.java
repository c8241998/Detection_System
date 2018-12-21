package com.czdsb.demo.service.impl;

import com.czdsb.demo.model.Camera;
import com.czdsb.demo.model.Record;
import com.czdsb.demo.model.User;
import com.czdsb.demo.service.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "cameraService")
public class CameraServiceImpl implements CameraService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void updateTime(String cameraId) {
        String sql = "UPDATE camera SET lastAccessedTime = now() WHERE cameraId = ?";
        jdbcTemplate.update(sql, cameraId);
    }

    @Override
    public void updateTime(Camera camera) {
        updateTime(camera.getCameraId());
    }

    @Override
    public Camera findById(String cameraId) {
        String sql = "select * from camera where cameraId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{cameraId}, cameraMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Camera> findByUser(User user) {
//        String sql = "select cameraId from cameraToUser where userId = ?";
        String sql = "SELECT camera.* FROM camera inner join cameratouser " +
                "where cameratouser.userId = ? and cameratouser.cameraId = camera.cameraId";

        try {
            return jdbcTemplate.query(sql, new Object[]{user.getUserId()}, cameraMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    private static RowMapper<Camera> cameraMapper = new BeanPropertyRowMapper<>(Camera.class);
    private static RowMapper<Record> recordMapper = new BeanPropertyRowMapper<>(Record.class);

    @Override
    public Record createRecord(Record record) {
        String sql = "INSERT INTO record(cameraId, invadeTime, invadeType) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, record.getCameraId(), record.getInvadeTime(), record.getInvadeType());
        sql = "select * from record where cameraId = ? and invadeTime = ? and invadeType = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{record.getCameraId(), record.getInvadeTime(), record.getInvadeType()}, recordMapper).get(0);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Record findRecords(String cameraId, Integer index) {
        String sql = "select * from record where cameraId = ? order by invadeTime desc limit ?, 1;";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{cameraId, index}, recordMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Record findRecords(Camera camera, Integer index) {
        return findRecords(camera.getCameraId(), index);
    }

    @Override
    public Integer findRecordsNum(Camera camera) {
        String sql = "select count(*) from record where cameraId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{camera.getCameraId()},Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}