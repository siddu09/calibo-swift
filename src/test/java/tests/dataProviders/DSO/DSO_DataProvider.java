package tests.dataProviders.DSO;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class DSO_DataProvider {

    public static Object[][] getExcelData(String filePath, String sheetName)
    {
        Object[][] data = null;
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getPhysicalNumberOfRows();
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getPhysicalNumberOfCells();

            // The first dimension represents the number of test execution rows
            data = new Object[rowCount - 1][1];

            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Map<String, String> map = new HashMap<>();

                for (int j = 0; j < colCount; j++) {
                    String columnName = formatter.formatCellValue(headerRow.getCell(j));
                    String cellValue = formatter.formatCellValue(row.getCell(j));
                    map.put(columnName, cellValue);
                }

                // CRITICAL FIX: Put the entire map into the single column array element
                data[i - 1][0] = map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}



