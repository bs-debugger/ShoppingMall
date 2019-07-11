package com.xq.live.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * poi解析
 */
public class ExcelUtils {
    /**
     * 解析excel  返回domain
     * @param file
     * @return
     * @throws Exception
     */
    public static List<Row> readExcel( MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        Workbook hssfWorkbook = null;
        List<Row> rowList=new ArrayList<Row>();
        if (file.getOriginalFilename().endsWith("xlsx")) {
            hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
        } else if (file.getOriginalFilename().endsWith("xls")) {
            hssfWorkbook = new HSSFWorkbook(is);//Excel 2003

        }
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                rowList.add(hssfSheet.getRow(rowNum));
            }
        }
        return rowList;
    }

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.autoSizeColumn(0);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (25 * 15));

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        //字体
        HSSFFont font = wb.createFont();
        String color = "cbfdee";    //此处得到的color为16进制的字符串
        //转为RGB码
        int r = Integer.parseInt((color.substring(0,2)),16);   //转为16进制
        int g = Integer.parseInt((color.substring(2,4)),16);
        int b = Integer.parseInt((color.substring(4,6)),16);
        //自定义cell颜色
        HSSFPalette palette = wb.getCustomPalette();
        //这里的9是索引
        palette.setColorAtIndex((short)9, (byte) r, (byte) g, (byte) b);
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style1.setFillForegroundColor((short)9);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        font.setFontName("微软雅黑");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        font.setFontHeightInPoints((short) 12);//设置字体大小
        style1.setFont(font);


        HSSFFont font2 = wb.createFont();
        font2.setFontName("微软雅黑");
        font2.setFontHeightInPoints((short) 10);

        //声明列对象
        HSSFCell cell = null;

        HSSFCellStyle setBorder = wb.createCellStyle();
        setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style1);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 35 / 15);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            row.setHeight((short) (25 * 15));
            if(values[i]==null){continue;}
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                style.setFont(font2);
                cell=row.createCell(j);
                cell.setCellValue(values[i][j]);
                cell.setCellStyle(style);
            }
        }
        return wb;
    }

    public static float getExcelCellAutoHeight(String str, float fontCountInline) {
        float defaultRowHeight = 12.00f;//每一行的高度指定
        float defaultCount = 0.00f;
        for (int i = 0; i < str.length(); i++) {
            float ff = getregex(str.substring(i, i + 1));
            defaultCount = defaultCount + ff;
        }
        return ((int) (defaultCount / fontCountInline) + 1) * defaultRowHeight;//计算
    }

    public static float getregex(String charStr) {

        if(charStr==" ")
        {
            return 0.5f;
        }
        // 判断是否为字母或字符
        if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
            return 0.5f;
        }
        // 判断是否为全角

        if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
            return 1.00f;
        }
        //全角符号 及中文
        if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
            return 1.00f;
        }
        return 0.5f;

    }

    public static String dataFromat(String data){
       return data==null?"":data;
    }

    /**
     * 下载文件时，针对不同浏览器，进行附件名的编码
     *
     * @param filename
     *            下载文件名
     * @param agent
     *            客户端浏览器
     * @return 编码后的下载附件名
     * @throws IOException
     */
    public static String encodeDownloadFilename(String filename, String agent)
            throws IOException {
        // 如果是火狐浏览器
        if (agent.contains("Firefox")) {
            filename = "=?UTF-8?B?"
                    + URLEncoder.encode(filename,"UTF-8")
                    + "?=";
            filename = filename.replaceAll("\r\n", "");
            // IE及其他浏览器
        } else {
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+"," ");
        }
        return filename;
    }
}
