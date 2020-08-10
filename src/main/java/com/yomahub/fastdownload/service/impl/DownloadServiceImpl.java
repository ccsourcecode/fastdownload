package com.yomahub.fastdownload.service.impl;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yomahub.fastdownload.PropConstants;
import com.yomahub.fastdownload.dao.DownloadFileDAO;
import com.yomahub.fastdownload.dao.PersonDAO;
import com.yomahub.fastdownload.dto.DownloadFileDto;
import com.yomahub.fastdownload.po.DownloadFile;
import com.yomahub.fastdownload.po.Person;
import com.yomahub.fastdownload.service.DownloadService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.nutz.filepool.FilePool;
import org.nutz.filepool.NutFilePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.yomahub.fastdownload.data.exporter.XlsExportor;

@Transactional
@Component("downloadService")
public class DownloadServiceImpl implements DownloadService {
	private static final Logger log = LoggerFactory.getLogger(DownloadServiceImpl.class);

	@Autowired
	private PersonDAO personDao;

	@Autowired
    private DownloadFileDAO downloadFileDao;

	@Autowired
	private PersonDAO personDAO;

	@Autowired
    private XlsExportor XlsExportor;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PropConstants propConstants;

	private ExecutorService threadPool;

	public void createTestData(){
		final String[] nameArr = new String[]{"关羽","张飞","孟获","诸葛亮","曹操","赵云","黄忠","太史慈","周瑜","刘备",
				"张辽","吕布","貂蝉","周仓","姜维","王平","魏延","司马懿","徐晃","马岱","曹植","典韦","许褚"};

		final String[] titleArr = new String[]{"攻城狮","鼓励狮","占卜师","天文家","文学家","科学家","生物学家","研究员"};

		String sql = "insert into t_person(name,age,address,mobile,email,company,title,create_time) values(?,?,?,?,?,?,?,?)";

		final Random random = new Random();

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, nameArr[random.nextInt(nameArr.length)]);
				ps.setInt(2, random.nextInt(100));
				ps.setString(3,"上海市西藏中路" + i + "号");
				ps.setString(4, "1370000120" + random.nextInt(9));
				ps.setString(5, "whoareu@163.com");
				ps.setString(6, "上海一二三四五六七八九十有限公司");
				ps.setString(7, titleArr[random.nextInt(titleArr.length)]);
				ps.setTimestamp(8,new Timestamp(System.currentTimeMillis()));
			}

			@Override
			public int getBatchSize() {
				return 50000;
			}
		});
	}

	public void generateBigDataFile(){
	    if (this.threadPool == null){
	        this.threadPool = Executors.newFixedThreadPool(2);
	    }
        this.threadPool.submit(new Thread(){//也可用Callable接口，这样就可以用future获得返回值
            @Override
            public void run() {
                DownloadFile df = null;
                try{
                    FilePool filePool = new NutFilePool(propConstants.getFilePoolPath());

                    df = new DownloadFile();
                    df.setFileStatus(DownloadFile.FILE_STATUS_PENDING);
                    df.setStartTime(new Date());
                    File f = filePool.createFile(".csv");
                    Long fileId = filePool.getFileId(f);
                    df.setFileId(fileId);
                    downloadFileDao.save(df);

                    String sql = "select * from t_person t";
                    XlsExportor.generateFileFromSql(sql, null, f);

                    df.setFinishTime(new Date());
                    df.setFileStatus(DownloadFile.FILE_STATUS_SUCCESS);
					downloadFileDao.save(df);
                }catch(Throwable t){
                    t.printStackTrace();
                    df.setFileStatus(DownloadFile.FILE_STATUS_FAIL);
                    downloadFileDao.save(df);
                }
            }
        });
	}

	public Map<String, ?> findDownloadFileList(DownloadFileDto dto, int page, int rows){
		Sort sort = Sort.by(Sort.Direction.DESC,"id");
		Pageable pageable = PageRequest.of(page-1,rows,sort);
		Page<DownloadFile> pageResult = downloadFileDao.findAll(pageable);

		Map<String, Object> map = new HashMap<>();
		map.put("total",pageResult.getTotalElements());
		map.put("rows",pageResult.getContent());
	    return map;
	}

	@Override
	public List<Person> findAllPerson() {
		return personDAO.findAll();
	}
}
