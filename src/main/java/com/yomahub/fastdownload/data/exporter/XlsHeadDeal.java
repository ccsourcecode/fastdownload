package com.yomahub.fastdownload.data.exporter;

/**
 * Xls头部扩展接口
 * @author Bryan Zhang
 */

import org.apache.poi.hssf.usermodel.HSSFSheet;

public interface XlsHeadDeal {
    public Integer dealHead(HSSFSheet sheet, Integer rowCnt) throws Exception;
}
