package com.czdsb.demo.model;

import java.util.Date;

public class Record {

    private Long recordId;
    private String cameraId;
    private Date invadeTime;
    private String invadeType;

    public Record() {
    }

    public Record(Long recordId, String cameraId, Date invadeTime, String invadeType) {
        this.recordId = recordId;
        this.cameraId = cameraId;
        this.invadeTime = invadeTime;
        this.invadeType = invadeType;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public Date getInvadeTime() {
        return invadeTime;
    }

    public void setInvadeTime(Date invadeTime) {
        this.invadeTime = invadeTime;
    }

    public String getInvadeType() {
        return invadeType;
    }

    public void setInvadeType(String invadeType) {
        this.invadeType = invadeType;
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", cameraId='" + cameraId + '\'' +
                ", invadeTime='" + invadeTime + '\'' +
                ", invadeType='" + invadeType + '\'' +
                '}';
    }
}
