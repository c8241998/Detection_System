package com.czdsb.demo.model;

import java.util.Date;

public class Camera {

    private String cameraId;
    private String password;
    private Date lastAccessedTime;

    public Camera() {
    }

    public Camera(String cameraId, String password) {
        this.cameraId = cameraId;
        this.password = password;
    }

    public Camera(String cameraId, String password, Date lastAccessedTime) {
        this.cameraId = cameraId;
        this.password = password;
        this.lastAccessedTime = lastAccessedTime;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(Date lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "cameraId='" + cameraId + '\'' +
                ", password='" + password + '\'' +
                ", lastAccessedTime=" + lastAccessedTime +
                '}';
    }
}
