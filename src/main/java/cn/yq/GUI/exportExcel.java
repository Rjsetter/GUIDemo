package cn.yq.GUI;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static cn.yq.File.FileUtil.judeDirExists;

/**
 * 接收传入的数据，用于生成EXCEL表格
 * @author RJSETTER
 * @date 2019/1/30
 */
public class exportExcel {
    private static Logger logger = LoggerFactory.getLogger(exportExcel.class);
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
    public static Sheet createSheet(Workbook workbook, String sheetName, List<String> sheetTitle, Map<String,List<String>> map){
        String safeName = WorkbookUtil.createSafeSheetName(sheetName);
        Sheet sheet = workbook.createSheet(safeName);
        //设置行数，以0为基础,设置标题
        int rowLine = 0;
        Row row =sheet.createRow(rowLine);
        for(int i=0;i<sheetTitle.size();i++){
            row.createCell(i).setCellValue(sheetTitle.get(i));
        }
        //输入内容
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            rowLine ++;
            //System.out.println(rowLine);
            Map.Entry entry = (Map.Entry) iter.next();
            List<String> msg = map.get(entry.getKey());
            Row newRow =sheet.createRow(rowLine);
            for(int i=0;i<msg.size();i++){
                newRow.createCell(i).setCellValue(msg.get(i));
            }
        }
        for(int i=0;i<sheetTitle.size();i++){
            //列表自适应宽度,要在内容
//            sheet.autoSizeColumn((short)i); //自动调整列宽
//            sheet.setColumnWidth(i, panduan.get(i).getBytes().length*256);
            sheet.setColumnWidth(i,3000);
        }
        return sheet;
    }

    /**
     *
     * @param excelName    文件名
     * @param title        第一行标题
     * @param MSG          Map来储存信息，key为需求号，value为内容
     */
    public static void createExecel(String excelName,List<String> title, Map<String,List<String>> MSG){
        //创建Excel对象
        Workbook workbook = createExcel();

        //输出Excel文件
        createSheet(workbook,excelName,title,MSG);
        try{
            //输出到桌面
            File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();
            String desktopPath = desktopDir.getAbsolutePath()+"\\JIRA表格\\";
            String filePath = desktopPath.replaceAll("\\\\","//");
            File dir =new File(filePath);
            //判断文件夹是否存在，不存在则创建它。
            judeDirExists(dir);
            FileOutputStream output=new FileOutputStream(filePath+excelName);

            logger.info("生成的excel输出地址为："+desktopPath+excelName);
            workbook.write(output);
            output.flush();
            output.close();
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
//        Sheet sheet = createSheet(workbook,"JIRA测试",title,MSG);
        FileOutputStream output=new FileOutputStream("D:\\GUIDemo\\workbook.xls");
        workbook.write(output);
        output.flush();
    }

}