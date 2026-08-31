package tests.ai;

import ai.mappers.RagEvaluationMapper;
import ai.models.DatasetSummary;
import ai.models.EvaluationRequest;
import ai.models.EvaluationResult;
import ai.models.RagEvaluationRecord;
import ai.readers.RagExcelReader;
import ai.services.EvaluationService;
import ai.services.EvaluationSummaryService;
import org.testng.annotations.Test;
import utils.AllureReportGenerator;
import utils.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class RagDatasetEvaluationTest {

    @Test
    public void validateDataset()
            throws Exception {

        long startTime =
                System.currentTimeMillis();


        String excelPath =
                "src/test/resources/testdata/files/AIML/SWIFT_RAG_Certification_Dataset.xlsx";

        RagExcelReader reader =
                new RagExcelReader();

        List<RagEvaluationRecord> records =
                reader.read(excelPath);

        RagEvaluationMapper mapper =
                new RagEvaluationMapper();

        EvaluationService evaluationService =
                new EvaluationService();

        List<EvaluationResult> results =
                new ArrayList<>();

        int count = 0;

        for (RagEvaluationRecord record : records) {

            EvaluationRequest request =
                    mapper.map(record);

            LoggerUtil.LOGGER.info(
                    "QUESTION={}",
                    request.getQuestion());

            LoggerUtil.LOGGER.info(
                    "ANSWER={}",
                    request.getAnswer());

            LoggerUtil.LOGGER.info(
                    "CONTEXT={}",
                    request.getContext());



            EvaluationResult result =
                    evaluationService.evaluate(
                            request);

            results.add(result);

            count++;

            if (count >= 10) {
                break;
            }
        }

        EvaluationSummaryService summaryService =
                new EvaluationSummaryService();

        DatasetSummary summary =
                summaryService.generateSummary(
                        results);

        AllureReportGenerator.attachDatasetSummary(
                summary);

        AllureReportGenerator.attachDetailedResults(
                results);

        LoggerUtil.LOGGER.info(
                "========================================");

        LoggerUtil.LOGGER.info(
                "AI DATASET SUMMARY");

        LoggerUtil.LOGGER.info(
                "========================================");

        LoggerUtil.LOGGER.info(
                "Total Records : {}",
                summary.getTotalRecords());

        LoggerUtil.LOGGER.info(
                "Strong Matches : {}",
                summary.getStrongMatches());

        LoggerUtil.LOGGER.info(
                "Likely Matches : {}",
                summary.getLikelyMatches());

        LoggerUtil.LOGGER.info(
                "Mismatches : {}",
                summary.getMismatches());

        LoggerUtil.LOGGER.info(
                "Passed Records : {}",
                summary.getPassedRecords());

        LoggerUtil.LOGGER.info(
                "Failed Records : {}",
                summary.getFailedRecords());

        LoggerUtil.LOGGER.info(
                "Pass Rate : {}%",
                String.format(
                        "%.2f",
                        summary.getPassRate()));

        LoggerUtil.LOGGER.info(
                "Average Score : {}",
                String.format(
                        "%.2f",
                        summary.getAverageScore()));

        LoggerUtil.LOGGER.info(
                "Highest Score : {}",
                String.format(
                        "%.2f",
                        summary.getHighestScore()));

        LoggerUtil.LOGGER.info(
                "Lowest Score : {}",
                String.format(
                        "%.2f",
                        summary.getLowestScore()));

        LoggerUtil.LOGGER.info(
                "========================================");

        long endTime =
                System.currentTimeMillis();

        LoggerUtil.LOGGER.info(
                "Execution Time={} ms",
                (endTime - startTime));

        LoggerUtil.LOGGER.info(
                "Dataset evaluation completed successfully.");
    }
}