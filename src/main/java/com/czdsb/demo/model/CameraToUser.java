package com.czdsb.demo.model;

public class CameraToUser {

    private String cameraId;
    private Integer userId;

    public CameraToUser() {
    }

    public CameraToUser(String cameraId, Integer userId) {
        this.cameraId = cameraId;
        this.userId = userId;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CameraToUser{" +
                "cameraId='" + cameraId + '\'' +
                ", userId=" + userId +
                '}';
    }
}
