package ai.readers;

import ai.models.RagEvaluationRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class RagExcelReader {

    public List<RagEvaluationRecord> read(
            String filePath)
            throws Exception {

        List<RagEvaluationRecord> records =
                new ArrayList<>();

        try (FileInputStream fis =
                     new FileInputStream(filePath);

             Workbook workbook =
                     new XSSFWorkbook(fis)) {

            Sheet sheet =
                    workbook.getSheetAt(0);

            for (int i = 1;
                 i <= sheet.getLastRowNum();
                 i++) {

                Row row =
                        sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String question =
                        getCellValue(row.getCell(0));

                String answer =
                        getCellValue(row.getCell(1));

                String context =
                        getCellValue(row.getCell(2));

                records.add(
                        RagEvaluationRecord
                                .builder()
                                .question(question)
                                .answer(answer)
                                .context(context)
                                .build());
            }
        }

        return records;
    }

    private String getCellValue(
            Cell cell) {

        return cell == null
                ? ""
                : cell.toString();
    }
}