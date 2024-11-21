package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {
    private static Workbook workbook;
    private static Sheet sheet;

    public static void setExcelFile(String filePath, String sheetName) throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(file);
        sheet = workbook.getSheet(sheetName);
    }

    public static String getCellData(int row, int col) {
        Row excelRow = sheet.getRow(row);
        Cell cell = excelRow.getCell(col);
        return cell.getStringCellValue();
    }

    public static int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }
}
