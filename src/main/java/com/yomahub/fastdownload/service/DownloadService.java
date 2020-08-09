package com.yomahub.fastdownload.service;

import com.yomahub.fastdownload.dto.DownloadFileDto;
import com.yomahub.fastdownload.po.Person;

import java.util.List;
import java.util.Map;

public interface DownloadService {

	void createTestData();

	List<Person> findAllPerson();

	void generateBigDataFile();

	Map<String, ?> findDownloadFileList(DownloadFileDto dto, int page, int rows);
}
