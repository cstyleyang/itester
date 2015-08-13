package com.bestpay.tools.test.dubbo.writer;

import cn.com.bestpay.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglingsi on 15/8/11.
 */
@Slf4j
public class ExcelPlanWriter {

    FileOutputStream output;

    InputStream is;

    HSSFWorkbook wk;

    Map<String,Integer> map;


    public void writeRow(HSSFRow row,String result,String planPath) throws IOException {

        log.info("测试结果：{}", "NoN_".equals(result) ? "执行结果异常" : result);

        for(int k =0; k < row.getLastCellNum();k++ ){
            HSSFCell cell = row.getCell(k);
            cell = null==cell ? row.createCell(k) : cell;
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);

            if (null != cell && "#".equals(cell.getStringCellValue())) {
                cell = row.getCell(k + 1);
                cell = null == cell ? row.createCell(k + 1) : cell;
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue("NoN_".equals(result) ? "服务名或方法名有误！" : result);

                HSSFCell cellExpect = row.getCell(k+2);
                HSSFCell cellResult = row.getCell(k+3);

                CellStyle style = wk.createCellStyle();

                cellExpect = null == cellExpect ? row.createCell(k + 2) : cellExpect;
                cellResult = null == cellResult ? row.createCell(k + 3) : cellResult;

                log.info("接口返回的Response :{}" ,"NoN_".equals(result) ? "工具异常：找不到服务" : result);
                log.info("excel中Expect :{}", cellExpect.getStringCellValue().trim());

                if ("NoN_".equals(result)){
                    style.setFillForegroundColor(IndexedColors.RED.getIndex());
                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);

                    cellResult.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cellResult.setCellStyle(style);
                    cellResult.setCellValue("失败");
                }else {
                    if("".equals(cellExpect.getStringCellValue())) { //预期没有填写
                        log.info("列{},行{} 预期没有填写。", cellExpect.getColumnIndex(), cellExpect.getRowIndex());
                    } else {
                        if(cell.getStringCellValue().equals(cellExpect.getStringCellValue().trim())) {
                            style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

                            cellResult.setCellType(HSSFCell.CELL_TYPE_STRING);
                            cellResult.setCellStyle(style);
                            cellResult.setCellValue("通过");
                        } else {
                            style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
                            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

                            cellResult.setCellType(HSSFCell.CELL_TYPE_STRING);
                            cellResult.setCellStyle(style);
                            cellResult.setCellValue("执行通过");
                        }
                    }
                }


                output = new FileOutputStream(planPath);
                wk.write(output);
            }
        }
        log.info("测试结果写入成功:{}",result);

    }

    public void writeResult(String planPath,List<String> ls) {
        try {
            is = new FileInputStream(planPath);

            wk = new HSSFWorkbook(is);

            map = new LinkedHashMap<String,Integer>();
            for (int j = 0; j < wk.getNumberOfSheets(); j++) {
                HSSFSheet sheet = wk.getSheetAt(j);
                for (int w = 3; w < sheet.getLastRowNum(); w++) {
                    HSSFRow row = sheet.getRow(w);
                    if (null != row && !"end".equals(row.getCell(0).getStringCellValue())) {
                        map.put(j+"_"+w,w);
                    }else {
                        break;
                    }
                }
            }

            int index = 0;

            for(Map.Entry<String,Integer> entry : map.entrySet()){
                String sheetNo = entry.getKey().split("_")[0];

                HSSFRow row = wk.getSheetAt(Integer.valueOf(sheetNo)).getRow(entry.getValue());

                if (ls.size() > 0) {
                    writeRow(row, ls.get(index), planPath);
                    index++;
                }else {
                    break;
                }
            }

            log.info("全部结果执行完毕，测试回写Excel完成！");
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
    }
}
