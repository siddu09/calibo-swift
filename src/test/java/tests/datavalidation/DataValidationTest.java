package tests.datavalidation;

import datavalidation.DataFrameValidator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import tech.tablesaw.api.Table;

public class DataValidationTest {

    @Test
    public void validateCSVData() {

        DataFrameValidator validator =
                new DataFrameValidator();

        Table users =
                validator.loadCSV(
                        "src/test/resources/testdata/users.csv");

        System.out.println(users.print());

        System.out.println(
                "[TABLESAW] Rows = "
                        + users.rowCount());

        System.out.println(
                "[TABLESAW] Columns = "
                        + users.columnCount());

        Assertions.assertThat(
                        users.rowCount())
                .isGreaterThan(0);

        Assertions.assertThat(
                        users.columnCount())
                .isEqualTo(3);
    }
}