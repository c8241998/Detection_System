package com.czdsb.demo.service;

import com.czdsb.demo.model.Camera;
import com.czdsb.demo.model.Record;
import com.czdsb.demo.model.User;

import java.util.List;

public interface CameraService {

    void updateTime(String cameraId);

    void updateTime(Camera camera);

    Camera findById(String cameraId);

    List<Camera> findByUser(User user);

    Record createRecord(Record record);

    Record findRecords(String cameraId, Integer index);

    Record findRecords(Camera camera, Integer index);

    Integer findRecordsNum(Camera camera);
}