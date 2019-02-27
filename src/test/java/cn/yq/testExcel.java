package cn.yq;

import jdk.internal.dynalink.support.BottomGuardingDynamicLinker;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class testExcel {
    //创建一个Excel对象，并简单的初始化后返回
    public static Workbook createExcel(){
        //创建Excel对象
        Workbook workbook = new HSSFWorkbook();
        //实例化样式对象
        CellStyle cellStyle =workbook.createCellStyle();
        // 两端对齐
        cellStyle.setAlignment(HorizontalAlignment.JUSTIFY);
        //垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置边框
        cellStyle.setBorderBottom(BorderStyle.SLANTED_DASH_DOT);
        // 边框颜色
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        // 日期展示格式
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        return workbook;
    }

    /**
     *
     * @param workbook
     * @param sheetName
     * @param sheetTitle
     * @param map<String,List<String>>   收集JIRA需求号，和内容List列表
     * @return
     */
    public static Sheet createSheet(Workbook workbook, String sheetName, String []sheetTitle, Map<String,List<String>> map){
        String safeName = WorkbookUtil.createSafeSheetName(sheetName);
        Sheet sheet = workbook.createSheet(safeName);
        //设置行数，以0为基础
        int rowLine = 0;
        Row row =sheet.createRow(rowLine);
        for(int i=0;i<sheetTitle.length;i++){
            row.createCell(i).setCellValue(sheetTitle[i]);
        }
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            rowLine ++;
            //System.out.println(rowLine);
            Map.Entry entry = (Map.Entry) iter.next();
            List<String> msg = map.get(entry.getKey());
            Row newRow =sheet.createRow(rowLine);
            for(int i=0;i<msg.size();i++){
                newRow.createCell(i).setCellValue(msg.get(i));
                //System.out.println(msg.get(i));
            }
        }
        return sheet;
    }

    public static void createExecel(String excelName,String []title, Map<String,List<String>> MSG){
        //创建Excel对象
        Workbook workbook = createExcel();

        //输出Excel文件
        Sheet sheet = createSheet(workbook,excelName,title,MSG);
        try{
            FileOutputStream output=new FileOutputStream("D:\\GUIDemo\\workboo.xls");
            workbook.write(output);
            output.flush();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String []args) throws IOException {
        //创建Excel对象
        Workbook workbook = createExcel();
        //第一行标题
        String []title = {"JIRA号","需求名称","开发负责人","测试负责人"};
        Map<String,List<String>> MSG = new HashMap<String, List<String>>();
        List<String> msg = new LinkedList<String>();
        msg.add("JIRA号");
        msg.add("JIRA号");
        msg.add("JIRA号");
        msg.add("JIRA号");
        MSG.put("a",msg);
        MSG.put("b",msg);
        //输出Excel文件
        Sheet sheet = createSheet(workbook,"JIRA测试",title,MSG);
        FileOutputStream output=new FileOutputStream("D:\\GUIDemo\\workbook.xls");
        workbook.write(output);
        output.flush();
    }

}
