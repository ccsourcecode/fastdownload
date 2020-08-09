package com.yomahub.fastdownload.data.exporter;

/**
 * Xls文件底部扩展接口
 * @author Bryan Zhang
 */

import org.apache.poi.hssf.usermodel.HSSFSheet;

public interface XlsBottomDeal {
    public Integer dealBottom(HSSFSheet sheet, Integer rowCnt) throws Exception;
}
