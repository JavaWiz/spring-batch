package com.javawiz.configuration;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.javawiz.entity.UserDetails;
 
@Component("userDetailsExcelWriter")
//@Scope("step")
public class UserDetailsExcelWriter implements ItemWriter<UserDetails> {
 
    private static final String FILE_NAME = "output/UserDetails";
    private static final String[] HEADERS = { "userId", "username", "firstName", "lastName", "gender" };
 
    private String outputFilename;
    private Workbook workbook;
    private CellStyle dataCellStyle;
    private int currRow = 0;
 
    private void addHeaders(Sheet sheet) {
 
        Workbook wb = sheet.getWorkbook();
 
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
 
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
 
        Row row = sheet.createRow(2);
        int col = 0;
 
        for (String header : HEADERS) {
            Cell cell = row.createCell(col);
            cell.setCellValue(header);
            cell.setCellStyle(style);
            col++;
        }
        currRow++;
    }
 
    private void addTitleToSheet(Sheet sheet) {
 
        Workbook wb = sheet.getWorkbook();
 
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
 
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Arial");
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
 
        Row row = sheet.createRow(currRow);
        row.setHeightInPoints(16);
 
        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellStyle(style);
 
        CellRangeAddress range = new CellRangeAddress(0, 0, 0, 7);
        sheet.addMergedRegion(range);
        currRow++;
 
    }
 
    @AfterStep
    public void afterStep(StepExecution stepExecution) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFilename);
        workbook.write(fos);
        fos.close();
    }
 
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Calling beforeStep");
        
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        outputFilename = FILE_NAME + "_" + dateTime + ".xlsx";
 
        workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet("Testing");
        sheet.createFreezePane(0, 3, 0, 3);
        sheet.setDefaultColumnWidth(20);
 
        addTitleToSheet(sheet);
        currRow++;
        addHeaders(sheet);
        initDataStyle();
 
    }
 
    private void initDataStyle() {
        dataCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
 
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        dataCellStyle.setAlignment(HorizontalAlignment.LEFT);
        dataCellStyle.setFont(font);
    }
 
    @Override
    public void write(List<? extends UserDetails> items) throws Exception {
 
        Sheet sheet = workbook.getSheetAt(0);
 
        for (UserDetails data : items) {
            currRow++;
            Row row = sheet.createRow(currRow);
            createStringCell(row, String.valueOf(data.getUserId()), 0);
            createStringCell(row, data.getUsername(), 1);
            createStringCell(row, data.getFirstName(), 2);
            createStringCell(row, data.getLastName(), 3);
            createStringCell(row, data.getGender(), 4);
        }
    }
 
    private void createStringCell(Row row, String val, int col) {
        Cell cell = row.createCell(col, CellType.STRING);
        cell.setCellValue(val);
    }
 
    private void createNumericCell(Row row, Double val, int col) {
        Cell cell = row.createCell(col, CellType.NUMERIC);
        cell.setCellValue(val);
    }
 
}