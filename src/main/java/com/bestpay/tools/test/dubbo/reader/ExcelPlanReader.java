package com.bestpay.tools.test.dubbo.reader;

import com.bestpay.tools.test.dubbo.caches.Cache;
import com.bestpay.tools.test.dubbo.model.ServiceDetail;
import com.bestpay.tools.test.dubbo.model.TestTask;
import com.bestpay.tools.test.dubbo.model.TypeValue;
import com.bestpay.tools.test.dubbo.util.GsonReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiaju on 2015/8/8.
 */
@Slf4j
public class ExcelPlanReader extends BasePlanReader{

    @Autowired
    Cache cache;

    public boolean read(String planPath, ClassPathXmlApplicationContext context) {

        try {
            readXls(planPath);
        }catch (Exception e){
            log.error("读Excel异常:{}",e);
        }
        return true;
    }

    public void  readXls(String planPath) throws IOException {
        InputStream is = new FileInputStream(planPath);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        TestTask testTask = null;
        testTaskList = new ArrayList<TestTask>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }

            ServiceDetail serviceDetail = getServiceDetail(hssfSheet.getRow(0));

            // 遍历表格,数据从第四行开始，第一行为接口信息，第二行为参数说明，第三行为Key值
            for (int rowNum = 3; rowNum < hssfSheet.getLastRowNum(); rowNum++) {
                testTask = new TestTask();
                setTestTaskByRow(testTask, hssfSheet.getRow(0));

                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                int paramNo = 0;

                List<TypeValue> paramList = new ArrayList<TypeValue>();
                Map<String, Object> map = new HashMap<String, Object>();
                for (int colNum = 0; colNum < hssfRow.getLastCellNum(); colNum++) {
                    //Key行
                    HSSFRow keyRow = hssfSheet.getRow(2);
                    String key = keyRow.getCell(colNum).getStringCellValue();
                    if("&".equals(key)){
                        String type = getParamType(serviceDetail, paramNo);
                        Object value = getParamValue(map);

                        //创建新的map为下个读取参数
                        map = new HashMap<String, Object>();
                        paramNo++;
                        TypeValue typeValue = new TypeValue(type, value);
                        paramList.add(typeValue);
                    }else if("#".equals(key)){
                        String type = getParamType(serviceDetail, paramNo);
                        Object value = getParamValue(map);

                        TypeValue typeValue = new TypeValue(type, value);
                        paramList.add(typeValue);
                        break;
                    }
                    else {
                        HSSFCell cell = hssfRow.getCell(colNum);
                        map.put(key, getValue(cell));
                    }
                }

                //任务组装完成
                testTask.setClazz(serviceDetail.getClazz());
                testTask.setParamList(paramList);
                testTaskList.add(testTask);
            }
        }
    }

    public ServiceDetail getServiceDetail(HSSFRow hssfRow){
        String key = hssfRow.getCell(0).getStringCellValue() + "_" +
                hssfRow.getCell(1).getStringCellValue();
        return cache.getServiceCache().get(key);
    }

    public String getParamType(ServiceDetail serviceDetail, int num){
        return serviceDetail.getParamClazzList().get(num);
    }

    public Object getParamValue(Map<String, Object> map){
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            if(map.size() == 1)
                return entry.getValue();
            else
                return GsonReader.readMapToJson(map);
        }
        return null;
    }

    public void setTestTaskByRow(TestTask testTask, HSSFRow hssfRow){
        testTask.setService(hssfRow.getCell(0).getStringCellValue());
        testTask.setMethod(hssfRow.getCell(1).getStringCellValue());
    }

    private Object getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return hssfCell.getBooleanCellValue();
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return hssfCell.getNumericCellValue();
        } else {
            // 返回字符串类型的值
            return hssfCell.getStringCellValue();
        }
    }

}
