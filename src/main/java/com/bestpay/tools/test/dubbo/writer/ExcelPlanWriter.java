package com.bestpay.tools.test.dubbo.writer;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
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

    String planPath = "plan.xls";


    public void writeRow(HSSFRow row,String result) throws IOException {

        log.info("测试结果：{}",result);

        for(int k =0; k < row.getLastCellNum();k++ ){
            HSSFCell cell = row.getCell(k);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            if (null != cell && "#".equals(cell.getStringCellValue())) {
                cell = row.getCell(k + 1);
                if (null == cell) {
                    cell = row.createCell(k + 1);
                }
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(result);
                output = new FileOutputStream(planPath);
                wk.write(output);
            }
        }
        log.info("测试结果写入成功:{}",result);

    }

    public void writeResult(List<String> ls) {
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

            log.info("记录写入cell坐标集合：" , map);

            int index = 0;


            for(Map.Entry<String,Integer> entry : map.entrySet()){
                String sheetNo = entry.getKey().split("_")[0];

                HSSFRow row = wk.getSheetAt(Integer.valueOf(sheetNo)).getRow(entry.getValue());

                writeRow(row,ls.get(index));
                index++;
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
