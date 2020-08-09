package com.yomahub.fastdownload.util;

import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class PoiUtil {
    public static void setCellValue(HSSFRow curRow,int colCnt, Object valueObj){
        try{
            if (valueObj instanceof Date){
                curRow.createCell(colCnt).setCellValue(new HSSFRichTextString(DateUtil.dateToStr((Date)valueObj)));
            }else if (valueObj instanceof Long){
                curRow.createCell(colCnt).setCellValue((Long)valueObj);
            }else if (valueObj instanceof Integer){
                curRow.createCell(colCnt).setCellValue((Integer)valueObj);
            }else if (valueObj instanceof Double){
                curRow.createCell(colCnt).setCellValue((Double)valueObj);
            }else if (valueObj instanceof Boolean){
                curRow.createCell(colCnt).setCellValue((Boolean)valueObj);
            }else{
                curRow.createCell(colCnt).setCellValue(new HSSFRichTextString(valueObj.toString()));
            }
        }catch (Exception e){
            curRow.createCell(colCnt).setCellValue(new HSSFRichTextString());
        }
    }
}
