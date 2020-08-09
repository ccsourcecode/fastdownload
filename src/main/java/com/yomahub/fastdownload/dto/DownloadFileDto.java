package com.yomahub.fastdownload.dto;

import java.io.Serializable;
import java.util.Date;

public class DownloadFileDto implements Serializable {

    private static final long serialVersionUID = -3883894355813014583L;

    private Long id;
    private Long fileId;
    private String fileStatus;
    private Date startTime;
    private Date finishTime;
    private String interval;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getFileId() {
        return fileId;
    }
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    public String getFileStatus() {
        return fileStatus;
    }
    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
    public String getInterval() {
        return interval;
    }
    public void setInterval(String interval) {
        this.interval = interval;
    }
}
