package utils;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {

    public Object[][] getSheetData(
            String filePath,
            String sheetName) {

        try {

            FileInputStream fis =
                    new FileInputStream(filePath);

            Workbook workbook =
                    WorkbookFactory.create(fis);

            Sheet sheet =
                    workbook.getSheet(sheetName);

            int rowCount =
                    sheet.getPhysicalNumberOfRows();

            int colCount =
                    sheet.getRow(0)
                            .getPhysicalNumberOfCells();

            Object[][] data =
                    new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {

                Row row = sheet.getRow(i);

                for (int j = 0; j < colCount; j++) {

                    data[i - 1][j] =
                            row.getCell(j).toString();
                }
            }

            workbook.close();

            return data;

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}