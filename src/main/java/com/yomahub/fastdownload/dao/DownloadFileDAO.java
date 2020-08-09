package com.yomahub.fastdownload.dao;

import com.yomahub.fastdownload.po.DownloadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadFileDAO extends JpaRepository<DownloadFile, Long> {
}
