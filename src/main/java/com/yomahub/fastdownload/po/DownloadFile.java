package com.yomahub.fastdownload.po;

import com.yomahub.fastdownload.util.DateUtil;

import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name = "t_download_file")
public class DownloadFile{

    public static final String FILE_STATUS_PENDING = "P";
    public static final String FILE_STATUS_SUCCESS = "S";
    public static final String FILE_STATUS_FAIL = "F";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="file_id")
    private Long fileId;

    @Column(name="file_status")
    private String fileStatus;

    @Column(name="start_time")
    private Date startTime;

    @Column(name="finish_time")
    private Date finishTime;

    @Transient
    private String interval;

    /** default constructor */
    public DownloadFile() {
    }

    /** full constructor */
    public DownloadFile(String fileStatus, Date startTime, Date finishTime) {
        this.fileStatus = fileStatus;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public Long getId() {
        return this.id;
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
        return this.fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public String getStartTimeStr() {
        return DateUtil.dateToStr(this.startTime);
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return this.finishTime;
    }

    public String getFinishTimeStr() {
        return DateUtil.dateToStr(this.finishTime);
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getInterval() {
        return DateUtil.getBetweenStr(this.startTime, this.finishTime);
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
