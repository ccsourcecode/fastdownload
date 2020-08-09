package com.yomahub.fastdownload.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.yomahub.fastdownload.PropConstants;
import com.yomahub.fastdownload.data.exporter.XlsBottomDeal;
import com.yomahub.fastdownload.data.exporter.XlsHeadDeal;
import com.yomahub.fastdownload.dto.DownloadFileDto;
import com.yomahub.fastdownload.po.Person;
import com.yomahub.fastdownload.service.DownloadService;
import com.yomahub.fastdownload.util.ServletUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nutz.filepool.FilePool;
import org.nutz.filepool.NutFilePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.yomahub.fastdownload.data.exporter.XlsExportor;

@Controller
public class DownloadController extends BaseController {

	@Autowired
	private DownloadService downloadService;

	@Autowired
    private XlsExportor XlsExportor;

	@Autowired
	private PropConstants propConstants;

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("/index");
		return modelAndView;
	}

	@RequestMapping("/testDownload1")
	public ModelAndView testDownload1(HttpServletRequest request,HttpServletResponse response) throws Exception {
		URL url = new URL("http://www.baidu.com");
		InputStream is = url.openStream();

		response.setHeader("Content-Disposition","attachment;filename=demo.txt");
		OutputStream out = response.getOutputStream();

		int n = 0;
		byte b[] = new byte[2*1024];
		while ((n = is.read(b)) != -1) {
			out.write(b, 0, n);
		}
		out.flush();
		out.close();
		is.close();
		return null;
	}

	@RequestMapping("/testDownload2")
	public ModelAndView testDownload2(HttpServletRequest request,HttpServletResponse response) throws Exception {
		List<Person> list = this.downloadService.findAllPerson();
		System.out.println(list.size());

		LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("age", "年龄");
		headMap.put("createTime", "创建日期");
		headMap.put("title", "职称");
		headMap.put("name", "姓名");

		Map<Integer, Integer> colWidthMap = new HashMap<Integer, Integer>();
		colWidthMap.put(0, 6000);
		colWidthMap.put(1, 6000);
		colWidthMap.put(2, 6000);
		colWidthMap.put(3, 6000);

		HSSFWorkbook wb = this.XlsExportor.convertList(list, headMap, colWidthMap, (sheet, rowCnt) -> {
			HSSFRow curRow = sheet.createRow(rowCnt++);
			curRow.createCell(0).setCellValue(new HSSFRichTextString("我是头部信息"));
			curRow = sheet.createRow(rowCnt++);
			curRow.createCell(0).setCellValue(new HSSFRichTextString("我是头部信息2"));
			return rowCnt;
		}, (sheet, rowCnt) -> {
			HSSFRow curRow = sheet.createRow(rowCnt++);
			curRow.createCell(0).setCellValue(new HSSFRichTextString("我是尾部信息"));
			return rowCnt;
		});
		response.setHeader("Content-Disposition","attachment;filename=demo.xls");
		wb.write(response.getOutputStream());
		return null;
	}

	@RequestMapping("/testDownload3")
    public ModelAndView testDownload3(HttpServletRequest request,HttpServletResponse response) throws Exception {
	    String message = null;
	    try{
	        this.downloadService.generateBigDataFile();
	        message = "提交成功";
	    }catch(Throwable t){
	        t.printStackTrace();
	        message = "提交失败";
	    }
	    ServletUtil.writerText(response, message);
        return null;
	}


	@RequestMapping("/createTestData")
	public ModelAndView createTestData(HttpServletRequest request,HttpServletResponse response) throws Exception {
		this.downloadService.createTestData();
		return null;
	}

	@RequestMapping("/listDownloadFile")
    public ModelAndView listDownloadFile(HttpServletRequest request,HttpServletResponse response, DownloadFileDto dto) throws Exception {
	    int page = Integer.parseInt(request.getParameter("page"));
	    int rows = Integer.parseInt(request.getParameter("rows"));
        Map<String, ?> dataMap = this.downloadService.findDownloadFileList(dto, page, rows);
        ServletUtil.writerJson(response, JSON.toJSONString(dataMap));
        return null;
    }

	@RequestMapping("/downloadFile")
    public ModelAndView downloadFile(HttpServletRequest request,HttpServletResponse response, Long fileId) throws Exception {
        System.out.println(fileId);
        if (fileId != null){
            FilePool filePool = new NutFilePool(propConstants.getFilePoolPath());
            File file = filePool.getFile(fileId, ".csv");
            if (file != null){
                InputStream is = new FileInputStream(file);

                response.setHeader("Content-Disposition","attachment;filename=demo.csv");
                OutputStream out = response.getOutputStream();

                int n = 0;
                byte b[] = new byte[2*1024];
                while ((n = is.read(b)) != -1) {
                    out.write(b, 0, n);
                }
                out.flush();
                out.close();
                is.close();
            }
        }
        return null;
    }
}
